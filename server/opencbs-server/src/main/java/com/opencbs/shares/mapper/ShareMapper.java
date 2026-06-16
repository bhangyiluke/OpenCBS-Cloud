package com.opencbs.shares.mapper;

import com.opencbs.shares.domain.ShareLot;
import com.opencbs.shares.domain.ShareTransaction;
import com.opencbs.shares.dto.ShareLotDto;
import com.opencbs.shares.dto.ShareTransactionDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareMapper {

    public ShareLotDto mapToShareLotDto(ShareLot lot, LocalDate reportDate) {
        ShareLotDto dto = new ShareLotDto();
        dto.setId(lot.getId());
        dto.setShareProduct(mapProduct(lot.getShareProduct()));
        dto.setProfileId(lot.getProfile().getId());
        dto.setProfileName(lot.getProfile().getName());
        dto.setQuantity(lot.getQuantity());
        dto.setAvailableQuantity(lot.getAvailableQuantity());
        dto.setUnitPrice(lot.getUnitPrice());
        dto.setTotalAmount(lot.getTotalAmount());
        dto.setAcquisitionDate(lot.getAcquisitionDate());
        dto.setAgeInDays(lot.getAcquisitionDate() == null || reportDate == null ? null : ChronoUnit.DAYS.between(lot.getAcquisitionDate(), reportDate));
        dto.setSourceTransactionId(lot.getSourceTransactionId());
        dto.setSourceTransactionType(lot.getSourceTransactionType() == null ? null : lot.getSourceTransactionType().name());
        dto.setStatus(lot.getStatus() == null ? null : lot.getStatus().name());
        return dto;
    }

    public List<ShareLotDto> mapToShareLotDto(List<ShareLot> lots, LocalDate reportDate) {
        return lots.stream()
                .map(lot -> this.mapToShareLotDto(lot, reportDate))
                .collect(Collectors.toList());
    }

    public ShareTransactionDto mapToShareTransactionDto(ShareTransaction transaction) {
        ShareTransactionDto dto = new ShareTransactionDto();
        dto.setId(transaction.getId());
        dto.setShareProduct(mapProduct(transaction.getShareProduct()));
        dto.setSourceProfileId(transaction.getSourceProfile() == null ? null : transaction.getSourceProfile().getId());
        dto.setSourceProfileName(transaction.getSourceProfile() == null ? null : transaction.getSourceProfile().getName());
        dto.setDestinationProfileId(transaction.getDestinationProfile() == null ? null : transaction.getDestinationProfile().getId());
        dto.setDestinationProfileName(transaction.getDestinationProfile() == null ? null : transaction.getDestinationProfile().getName());
        dto.setType(transaction.getType() == null ? null : transaction.getType().name());
        dto.setStatus(transaction.getStatus() == null ? null : transaction.getStatus().name());
        dto.setQuantity(transaction.getQuantity());
        dto.setUnitPrice(transaction.getUnitPrice());
        dto.setTotalAmount(transaction.getTotalAmount());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setReason(transaction.getReason());
        dto.setIdempotencyKey(transaction.getIdempotencyKey());
        return dto;
    }

    public List<ShareTransactionDto> mapToShareTransactionDto(List<ShareTransaction> transactions) {
        return transactions.stream()
                .map(this::mapToShareTransactionDto)
                .collect(Collectors.toList());
    }

    private com.opencbs.shares.dto.ShareProductDetailsDto mapProduct(com.opencbs.shares.domain.ShareProduct product) {
        if (product == null) {
            return null;
        }
        com.opencbs.shares.dto.ShareProductDetailsDto dto = new com.opencbs.shares.dto.ShareProductDetailsDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCode(product.getCode());
        return dto;
    }
}
