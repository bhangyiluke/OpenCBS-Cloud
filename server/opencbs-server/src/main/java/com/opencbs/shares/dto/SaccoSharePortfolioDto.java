package com.opencbs.shares.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaccoSharePortfolioDto {
    private Long totalIssuedShares;
    private BigDecimal totalShareValue;
    private List<ShareProductSummaryDto> byProduct;
    private List<ShareAgeBucketDto> byAgeBucket;
    private List<MemberStatusSummaryDto> byMemberStatus;
}
