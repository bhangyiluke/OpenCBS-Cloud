package com.opencbs.shares.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareProductSummaryDto {
    private Long shareProductId;
    private String shareProductCode;
    private String shareProductName;
    private Long quantity;
    private BigDecimal value;
}
