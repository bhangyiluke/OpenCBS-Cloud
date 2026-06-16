package com.opencbs.shares.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShareAgeAnalysisDto {
    private Long totalQuantity;
    private BigDecimal totalValue;
    private List<ShareAgeBucketDto> buckets;
}
