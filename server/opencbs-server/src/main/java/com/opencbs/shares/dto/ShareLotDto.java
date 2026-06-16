package com.opencbs.shares.dto;

import com.opencbs.core.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ShareLotDto extends BaseDto {
    private ShareProductDetailsDto shareProduct;
    private Long profileId;
    private String profileName;
    private Long quantity;
    private Long availableQuantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private LocalDate acquisitionDate;
    private Long ageInDays;
    private Long sourceTransactionId;
    private String sourceTransactionType;
    private String status;
}
