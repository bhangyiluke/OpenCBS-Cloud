package com.opencbs.shares.validators;

import com.opencbs.core.annotations.Validator;
import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.services.ProfileService;
import com.opencbs.core.validators.BaseValidator;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.domain.enums.ShareLotStatus;
import com.opencbs.shares.dto.PurchaseShareDto;
import com.opencbs.shares.dto.TransferShareDto;
import com.opencbs.shares.repositories.ShareLotRepository;
import com.opencbs.shares.services.ShareProductService;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Validator
public class ShareTransactionValidator extends BaseValidator {

    private final ShareProductService shareProductService;
    private final ProfileService profileService;
    private final ShareLotRepository shareLotRepository;

    public ShareTransactionValidator(@NonNull ShareProductService shareProductService,
                                     @NonNull ProfileService profileService,
                                     @NonNull ShareLotRepository shareLotRepository) {
        this.shareProductService = shareProductService;
        this.profileService = profileService;
        this.shareLotRepository = shareLotRepository;
    }

    public void validatePurchase(PurchaseShareDto dto) {
        Assert.notNull(dto.getProfileId(), "Member is required.");
        Assert.isTrue(profileService.findOne(dto.getProfileId()).isPresent(), "Member is not found.");
        ShareProduct product = getProduct(dto.getShareProductId());
        Assert.notNull(dto.getQuantity(), "Share quantity is required.");
        Assert.isTrue(dto.getQuantity() > 0, "Share quantity must be greater than zero.");
        Assert.isTrue(dto.getQuantity() >= product.getMinSharesPerMember(), "Share purchase quantity is below the configured minimum.");
        Long currentQuantity = shareLotRepository.sumQuantityByProfileAndProduct(dto.getProfileId(), product.getId(), ShareLotStatus.ACTIVE);
        if (product.getMaxSharesPerMember() != null) {
            Assert.isTrue(currentQuantity + dto.getQuantity() <= product.getMaxSharesPerMember(), "Share purchase would exceed the configured maximum shareholding.");
        }
        Assert.notNull(dto.getUnitPrice(), "Share unit price is required.");
        Assert.isTrue(dto.getUnitPrice().compareTo(BigDecimal.ZERO) > 0, "Share unit price must be greater than zero.");
        Assert.isTrue(dto.getUnitPrice().compareTo(product.getUnitPrice()) == 0, "Share unit price does not match the configured product price.");
    }

    public void validateTransfer(TransferShareDto dto) {
        Assert.notNull(dto.getSourceProfileId(), "Source member is required.");
        Assert.notNull(dto.getDestinationProfileId(), "Destination member is required.");
        Assert.isTrue(profileService.findOne(dto.getSourceProfileId()).isPresent(), "Source member is not found.");
        Assert.isTrue(profileService.findOne(dto.getDestinationProfileId()).isPresent(), "Destination member is not found.");
        Assert.isTrue(!dto.getSourceProfileId().equals(dto.getDestinationProfileId()), "Source and destination members must be different.");
        ShareProduct product = getProduct(dto.getShareProductId());
        Assert.isTrue(product.getAllowMemberTransfers(), "Member-to-member share transfers are not allowed for this share product.");
        Assert.notNull(dto.getQuantity(), "Share quantity is required.");
        Assert.isTrue(dto.getQuantity() > 0, "Share quantity must be greater than zero.");
        Long sourceQuantity = shareLotRepository.sumQuantityByProfileAndProduct(dto.getSourceProfileId(), product.getId(), ShareLotStatus.ACTIVE);
        Assert.isTrue(sourceQuantity >= dto.getQuantity(), "Source member does not have enough available shares.");
        Long destinationQuantity = shareLotRepository.sumQuantityByProfileAndProduct(dto.getDestinationProfileId(), product.getId(), ShareLotStatus.ACTIVE);
        if (product.getMaxSharesPerMember() != null) {
            Assert.isTrue(destinationQuantity + dto.getQuantity() <= product.getMaxSharesPerMember(), "Share transfer would exceed the configured maximum shareholding.");
        }
        Assert.notNull(dto.getUnitPrice(), "Share unit price is required.");
        Assert.isTrue(dto.getUnitPrice().compareTo(BigDecimal.ZERO) > 0, "Share unit price must be greater than zero.");
        Assert.isTrue(dto.getUnitPrice().compareTo(product.getUnitPrice()) == 0, "Share unit price does not match the configured product price.");
    }

    private ShareProduct getProduct(Long productId) {
        Assert.notNull(productId, "Share product is required.");
        ShareProduct product = shareProductService.getOne(productId)
                .orElseThrow(() -> new IllegalArgumentException("Share product is not found."));
        Assert.isTrue(product.getStatusType() == StatusType.ACTIVE, "Share product is not active.");
        return product;
    }
}
