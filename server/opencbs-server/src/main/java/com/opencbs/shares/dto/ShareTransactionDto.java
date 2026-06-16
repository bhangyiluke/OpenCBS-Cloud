package com.opencbs.shares.dto;

import com.opencbs.core.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ShareTransactionDto extends BaseDto {
    private ShareProductDetailsDto shareProduct;
    private Long sourceProfileId;
    private String sourceProfileName;
    private Long destinationProfileId;
    private String destinationProfileName;
    private String type;
    private String status;
    private Long quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private LocalDate transactionDate;
    private String reason;
    private String idempotencyKey;
}
