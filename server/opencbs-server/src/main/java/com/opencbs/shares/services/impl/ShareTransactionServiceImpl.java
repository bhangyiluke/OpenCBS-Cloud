package com.opencbs.shares.services.impl;

import com.opencbs.core.domain.User;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.core.exceptions.ResourceNotFoundException;
import com.opencbs.core.services.ProfileService;
import com.opencbs.shares.domain.ShareLot;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.domain.ShareTransaction;
import com.opencbs.shares.domain.enums.ShareLotSelectionPolicy;
import com.opencbs.shares.domain.enums.ShareLotStatus;
import com.opencbs.shares.domain.enums.ShareTransactionStatus;
import com.opencbs.shares.domain.enums.ShareTransactionType;
import com.opencbs.shares.dto.*;
import com.opencbs.shares.mapper.ShareMapper;
import com.opencbs.shares.mapper.ShareProductMapper;
import com.opencbs.shares.repositories.ShareLotRepository;
import com.opencbs.shares.repositories.ShareProductRepository;
import com.opencbs.shares.repositories.ShareTransactionRepository;
import com.opencbs.shares.services.ShareProductService;
import com.opencbs.shares.services.ShareTransactionService;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShareTransactionServiceImpl implements ShareTransactionService {

    private final ShareProductService shareProductService;
    private final ShareProductRepository shareProductRepository;
    private final ShareLotRepository shareLotRepository;
    private final ShareTransactionRepository shareTransactionRepository;
    private final ProfileService profileService;
    private final ShareMapper shareMapper;
    private final ShareProductMapper shareProductMapper;

    public ShareTransactionServiceImpl(@NonNull ShareProductService shareProductService,
                                       @NonNull ShareProductRepository shareProductRepository,
                                       @NonNull ShareLotRepository shareLotRepository,
                                       @NonNull ShareTransactionRepository shareTransactionRepository,
                                       @NonNull ProfileService profileService,
                                       @NonNull ShareMapper shareMapper,
                                       @NonNull ShareProductMapper shareProductMapper) {
        this.shareProductService = shareProductService;
        this.shareProductRepository = shareProductRepository;
        this.shareLotRepository = shareLotRepository;
        this.shareTransactionRepository = shareTransactionRepository;
        this.profileService = profileService;
        this.shareMapper = shareMapper;
        this.shareProductMapper = shareProductMapper;
    }

    @Override
    @Transactional
    public ShareTransaction purchaseShares(PurchaseShareDto dto, User actor) {
        if (dto.getIdempotencyKey() != null) {
            Optional<ShareTransaction> existing = shareTransactionRepository.findByIdempotencyKey(dto.getIdempotencyKey());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        ShareProduct product = getActiveProduct(dto.getShareProductId());
        Profile profile = getProfile(dto.getProfileId());
        BigDecimal unitPrice = getUnitPrice(product, dto.getUnitPrice());
        validatePurchase(dto, product, profile, unitPrice);

        ShareTransaction transaction = createTransaction(ShareTransactionType.PURCHASE, product, null, profile, profile.getBranch(),
                dto.getQuantity(), unitPrice, dto.getTransactionDate(), null, dto.getIdempotencyKey(), actor);
        transaction.setStatus(ShareTransactionStatus.COMPLETED);
        ShareTransaction savedTransaction = shareTransactionRepository.save(transaction);

        ShareLot lot = createLot(product, profile, profile.getBranch(), dto.getQuantity(), unitPrice,
                dto.getTransactionDate(), savedTransaction.getId(), ShareTransactionType.PURCHASE, actor);
        shareLotRepository.save(lot);

        return savedTransaction;
    }

    @Override
    @Transactional
    public ShareTransaction transferShares(TransferShareDto dto, User actor) {
        if (dto.getIdempotencyKey() != null) {
            Optional<ShareTransaction> existing = shareTransactionRepository.findByIdempotencyKey(dto.getIdempotencyKey());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        ShareProduct product = getActiveProduct(dto.getShareProductId());
        Profile sourceProfile = getProfile(dto.getSourceProfileId());
        Profile destinationProfile = getProfile(dto.getDestinationProfileId());
        BigDecimal unitPrice = getUnitPrice(product, dto.getUnitPrice());
        validateTransfer(dto, product, sourceProfile, destinationProfile, unitPrice);

        ShareTransaction transaction = createTransaction(ShareTransactionType.TRANSFER, product, sourceProfile, destinationProfile,
                destinationProfile.getBranch(), dto.getQuantity(), unitPrice, dto.getTransactionDate(), dto.getReason(),
                dto.getIdempotencyKey(), actor);
        transaction.setStatus(ShareTransactionStatus.COMPLETED);
        ShareTransaction savedTransaction = shareTransactionRepository.save(transaction);

        List<ShareLot> sourceLots = getSourceLotsForTransfer(dto, product, sourceProfile);
        Long remaining = dto.getQuantity();
        for (ShareLot sourceLot : sourceLots) {
            if (remaining <= 0) {
                break;
            }
            Long take = Math.min(remaining, sourceLot.getAvailableQuantity());
            sourceLot.setAvailableQuantity(sourceLot.getAvailableQuantity() - take);
            shareLotRepository.save(sourceLot);

            ShareLot destinationLot = createLot(product, destinationProfile, destinationProfile.getBranch(), take,
                    sourceLot.getUnitPrice(), sourceLot.getAcquisitionDate(), savedTransaction.getId(),
                    ShareTransactionType.TRANSFER, actor);
            shareLotRepository.save(destinationLot);
            remaining = remaining - take;
        }

        if (remaining > 0) {
            throw new IllegalStateException("Source member does not have enough available shares.");
        }

        return savedTransaction;
    }

    private List<ShareLot> getSourceLotsForTransfer(TransferShareDto dto, ShareProduct product, Profile sourceProfile) {
        if (dto.getLotId() == null) {
            List<ShareLot> sourceLots = shareLotRepository.findAvailableLotsByProfile(sourceProfile.getId(), ShareLotStatus.ACTIVE);
            if (product.getLotSelectionPolicy() == ShareLotSelectionPolicy.LIFO) {
                Collections.reverse(sourceLots);
            }
            return sourceLots;
        }

        ShareLot sourceLot = shareLotRepository.findAvailableLotById(dto.getLotId(), ShareLotStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Share lot not found (ID=%d).", dto.getLotId())));
        if (!sourceLot.getProfile().getId().equals(sourceProfile.getId())) {
            throw new IllegalStateException("Share lot does not belong to source member.");
        }
        if (!sourceLot.getShareProduct().getId().equals(product.getId())) {
            throw new IllegalStateException("Share lot product does not match selected share product.");
        }
        if (sourceLot.getAvailableQuantity() < dto.getQuantity()) {
            throw new IllegalStateException("Selected share lot does not have enough available shares.");
        }
        return Collections.singletonList(sourceLot);
    }

    @Override
    public Page<ShareTransaction> searchTransactions(ShareTransactionFilterDto filter, Pageable pageable) {
        ShareTransactionFilterDto safeFilter = filter == null ? new ShareTransactionFilterDto() : filter;
        return shareTransactionRepository.search(pageable, safeFilter.getProfileId(), safeFilter.getShareProductId(),
                safeFilter.getType(), safeFilter.getStatus(), safeFilter.getBranchId(), safeFilter.getStartDate(), safeFilter.getEndDate());
    }

    @Override
    public Page<ShareLot> searchLots(Long profileId, Long shareProductId, Pageable pageable) {
        return shareLotRepository.searchAvailableLotsByProfile(pageable, profileId, shareProductId, ShareLotStatus.ACTIVE);
    }

    @Override
    public ShareAgeAnalysisDto analyzeAge(ShareAgeAnalysisFilterDto filter) {
        ShareAgeAnalysisFilterDto safeFilter = filter == null ? new ShareAgeAnalysisFilterDto() : filter;
        LocalDate reportDate = safeFilter.getReportDate() == null ? LocalDate.now() : safeFilter.getReportDate();
        List<ShareLot> lots = shareLotRepository.searchLots(safeFilter.getProfileId(), safeFilter.getShareProductId(),
                safeFilter.getBranchId(), ShareLotStatus.ACTIVE);
        return buildAgeAnalysis(lots, reportDate);
    }

    @Override
    public SaccoSharePortfolioDto saccoPortfolio(SharePortfolioFilterDto filter) {
        SharePortfolioFilterDto safeFilter = filter == null ? new SharePortfolioFilterDto() : filter;
        LocalDate reportDate = safeFilter.getReportDate() == null ? LocalDate.now() : safeFilter.getReportDate();
        List<ShareLot> lots = shareLotRepository.searchLots(null, safeFilter.getShareProductId(), safeFilter.getBranchId(), ShareLotStatus.ACTIVE);

        SaccoSharePortfolioDto dto = new SaccoSharePortfolioDto();
        dto.setTotalIssuedShares(lots.stream().mapToLong(ShareLot::getQuantity).sum());
        dto.setTotalShareValue(lots.stream()
                .map(lot -> lot.getUnitPrice().multiply(BigDecimal.valueOf(lot.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setByProduct(buildProductSummaries(lots));
        dto.setByAgeBucket(buildAgeBuckets(lots, reportDate));
        dto.setByMemberStatus(buildMemberStatusSummaries(lots));
        return dto;
    }

    @Override
    public MemberSharePortfolioDto memberPortfolio(Long profileId) {
        Profile profile = getProfile(profileId);
        List<ShareLot> lots = shareLotRepository.findByProfileId(profileId);
        Page<ShareTransaction> transactions = searchTransactions(new ShareTransactionFilterDto(), org.springframework.data.domain.PageRequest.of(0, 100));
        List<ShareTransaction> memberTransactions = transactions.getContent().stream()
                .filter(transaction -> transaction.getSourceProfile() != null && transaction.getSourceProfile().getId().equals(profileId)
                        || transaction.getDestinationProfile() != null && transaction.getDestinationProfile().getId().equals(profileId))
                .collect(Collectors.toList());

        MemberSharePortfolioDto dto = new MemberSharePortfolioDto();
        dto.setProfileId(profile.getId());
        dto.setProfileName(profile.getName());
        dto.setTotalQuantity(lots.stream().mapToLong(ShareLot::getQuantity).sum());
        dto.setTotalValue(lots.stream()
                .map(lot -> lot.getUnitPrice().multiply(BigDecimal.valueOf(lot.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setLots(shareMapper.mapToShareLotDto(lots, LocalDate.now()));
        dto.setTransactions(shareMapper.mapToShareTransactionDto(memberTransactions));
        return dto;
    }

    private ShareProduct getActiveProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Share product is required.");
        }
        ShareProduct product = shareProductRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Share product not found (ID=%d).", productId)));
        if (product.getStatusType() != com.opencbs.core.domain.enums.StatusType.ACTIVE) {
            throw new IllegalStateException("Share product is not active.");
        }
        return product;
    }

    private Profile getProfile(Long profileId) {
        if (profileId == null) {
            throw new IllegalArgumentException("Profile is required.");
        }
        return profileService.findOne(profileId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Profile not found (ID=%d).", profileId)));
    }

    private BigDecimal getUnitPrice(ShareProduct product, BigDecimal requestedUnitPrice) {
        BigDecimal unitPrice = requestedUnitPrice == null ? product.getUnitPrice() : requestedUnitPrice;
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Share unit price must be greater than zero.");
        }
        if (unitPrice.compareTo(product.getUnitPrice()) != 0) {
            throw new IllegalArgumentException("Share unit price does not match the configured product price.");
        }
        return unitPrice;
    }

    private void validatePurchase(PurchaseShareDto dto, ShareProduct product, Profile profile, BigDecimal unitPrice) {
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Share quantity must be greater than zero.");
        }
        if (dto.getQuantity() < product.getMinSharesPerMember()) {
            throw new IllegalArgumentException("Share purchase quantity is below the configured minimum.");
        }
        Long currentQuantity = shareLotRepository.sumQuantityByProfileAndProduct(profile.getId(), product.getId(), ShareLotStatus.ACTIVE);
        if (product.getMaxSharesPerMember() != null && currentQuantity + dto.getQuantity() > product.getMaxSharesPerMember()) {
            throw new IllegalArgumentException("Share purchase would exceed the configured maximum shareholding.");
        }
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Share purchase amount must be greater than zero.");
        }
    }

    private void validateTransfer(TransferShareDto dto, ShareProduct product, Profile sourceProfile, Profile destinationProfile, BigDecimal unitPrice) {
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Share quantity must be greater than zero.");
        }
        if (sourceProfile.getId().equals(destinationProfile.getId())) {
            throw new IllegalArgumentException("Share transfer source and destination members must be different.");
        }
        if (!product.getAllowMemberTransfers()) {
            throw new IllegalArgumentException("Member-to-member share transfers are not allowed for this share product.");
        }
        Long currentSourceQuantity = shareLotRepository.sumQuantityByProfileAndProduct(sourceProfile.getId(), product.getId(), ShareLotStatus.ACTIVE);
        if (currentSourceQuantity < dto.getQuantity()) {
            throw new IllegalArgumentException("Source member does not have enough available shares.");
        }
        Long currentDestinationQuantity = shareLotRepository.sumQuantityByProfileAndProduct(destinationProfile.getId(), product.getId(), ShareLotStatus.ACTIVE);
        if (product.getMaxSharesPerMember() != null && currentDestinationQuantity + dto.getQuantity() > product.getMaxSharesPerMember()) {
            throw new IllegalArgumentException("Share transfer would exceed the configured maximum shareholding.");
        }
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Share transfer amount must be greater than zero.");
        }
    }

    private ShareTransaction createTransaction(ShareTransactionType type, ShareProduct product, Profile sourceProfile, Profile destinationProfile,
                                               com.opencbs.core.domain.Branch branch, Long quantity, BigDecimal unitPrice, LocalDate transactionDate,
                                               String reason, String idempotencyKey, User actor) {
        ShareTransaction transaction = new ShareTransaction();
        transaction.setShareProduct(product);
        transaction.setSourceProfile(sourceProfile);
        transaction.setDestinationProfile(destinationProfile);
        transaction.setBranch(branch);
        transaction.setType(type);
        transaction.setQuantity(quantity);
        transaction.setUnitPrice(unitPrice);
        transaction.setTotalAmount(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        transaction.setTransactionDate(transactionDate == null ? LocalDate.now() : transactionDate);
        transaction.setReason(reason);
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setCreatedBy(actor);
        return transaction;
    }

    private ShareLot createLot(ShareProduct product, Profile profile, com.opencbs.core.domain.Branch branch, Long quantity, BigDecimal unitPrice,
                               LocalDate acquisitionDate, Long sourceTransactionId, ShareTransactionType sourceTransactionType, User actor) {
        ShareLot lot = new ShareLot();
        lot.setShareProduct(product);
        lot.setProfile(profile);
        lot.setBranch(branch);
        lot.setQuantity(quantity);
        lot.setAvailableQuantity(quantity);
        lot.setUnitPrice(unitPrice);
        lot.setTotalAmount(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        lot.setAcquisitionDate(acquisitionDate == null ? LocalDate.now() : acquisitionDate);
        lot.setSourceTransactionId(sourceTransactionId);
        lot.setSourceTransactionType(sourceTransactionType);
        lot.setStatus(ShareLotStatus.ACTIVE);
        lot.setCreatedBy(actor);
        return lot;
    }

    private ShareAgeAnalysisDto buildAgeAnalysis(List<ShareLot> lots, LocalDate reportDate) {
        ShareAgeAnalysisDto dto = new ShareAgeAnalysisDto();
        dto.setTotalQuantity(lots.stream().mapToLong(ShareLot::getAvailableQuantity).sum());
        dto.setTotalValue(lots.stream()
                .map(lot -> lot.getUnitPrice().multiply(BigDecimal.valueOf(lot.getAvailableQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setBuckets(buildAgeBuckets(lots, reportDate));
        return dto;
    }

    private List<ShareAgeBucketDto> buildAgeBuckets(List<ShareLot> lots, LocalDate reportDate) {
        List<ShareAgeBucketDto> buckets = new ArrayList<>();
        buckets.add(newBucket("0-30 days"));
        buckets.add(newBucket("31-90 days"));
        buckets.add(newBucket("91-180 days"));
        buckets.add(newBucket("181-365 days"));
        buckets.add(newBucket("more than 365 days"));

        for (ShareLot lot : lots) {
            ShareAgeBucketDto bucket = getBucket(buckets, lot.getAcquisitionDate(), reportDate);
            bucket.setQuantity(bucket.getQuantity() + lot.getAvailableQuantity());
            bucket.setValue(bucket.getValue().add(lot.getUnitPrice().multiply(BigDecimal.valueOf(lot.getAvailableQuantity()))));
        }
        return buckets;
    }

    private ShareAgeBucketDto newBucket(String label) {
        ShareAgeBucketDto bucket = new ShareAgeBucketDto();
        bucket.setLabel(label);
        bucket.setQuantity(0L);
        bucket.setValue(BigDecimal.ZERO);
        return bucket;
    }

    private ShareAgeBucketDto getBucket(List<ShareAgeBucketDto> buckets, LocalDate acquisitionDate, LocalDate reportDate) {
        long age = acquisitionDate == null ? 0 : Math.max(0, ChronoUnit.DAYS.between(acquisitionDate, reportDate));
        if (age <= 30) {
            return buckets.get(0);
        }
        if (age <= 90) {
            return buckets.get(1);
        }
        if (age <= 180) {
            return buckets.get(2);
        }
        if (age <= 365) {
            return buckets.get(3);
        }
        return buckets.get(4);
    }

    private List<ShareProductSummaryDto> buildProductSummaries(List<ShareLot> lots) {
        Map<Long, ShareProductSummaryDto> summaries = new LinkedHashMap<>();
        for (ShareLot lot : lots) {
            ShareProductSummaryDto summary = summaries.computeIfAbsent(lot.getShareProduct().getId(), id -> {
                ShareProductSummaryDto dto = new ShareProductSummaryDto();
                dto.setShareProductId(id);
                dto.setShareProductCode(lot.getShareProduct().getCode());
                dto.setShareProductName(lot.getShareProduct().getName());
                dto.setQuantity(0L);
                dto.setValue(BigDecimal.ZERO);
                return dto;
            });
            summary.setQuantity(summary.getQuantity() + lot.getQuantity());
            summary.setValue(summary.getValue().add(lot.getUnitPrice().multiply(BigDecimal.valueOf(lot.getQuantity()))));
        }
        return new ArrayList<>(summaries.values());
    }

    private List<MemberStatusSummaryDto> buildMemberStatusSummaries(List<ShareLot> lots) {
        Map<String, MemberStatusSummaryDto> summaries = new LinkedHashMap<>();
        for (ShareLot lot : lots) {
            String status = lot.getProfile().getStatus() == null ? "UNKNOWN" : lot.getProfile().getStatus().name();
            MemberStatusSummaryDto summary = summaries.computeIfAbsent(status, key -> {
                MemberStatusSummaryDto dto = new MemberStatusSummaryDto();
                dto.setStatus(key);
                dto.setQuantity(0L);
                dto.setValue(BigDecimal.ZERO);
                return dto;
            });
            summary.setQuantity(summary.getQuantity() + lot.getQuantity());
            summary.setValue(summary.getValue().add(lot.getUnitPrice().multiply(BigDecimal.valueOf(lot.getQuantity()))));
        }
        return new ArrayList<>(summaries.values());
    }
}
