package com.opencbs.shares.dto;

import com.opencbs.core.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransferShareDto extends BaseDto {
    private Long sourceProfileId;
    private Long destinationProfileId;
    private Long shareProductId;
    private Long quantity;
    private Long lotId;
    private BigDecimal unitPrice;
    private LocalDate transactionDate;
    private String reason;
    private String idempotencyKey;
}
