package com.opencbs.shares.dto;

import com.opencbs.core.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseShareDto extends BaseDto {
    private Long profileId;
    private Long shareProductId;
    private Long quantity;
    private BigDecimal unitPrice;
    private LocalDate transactionDate;
    private String idempotencyKey;
}
