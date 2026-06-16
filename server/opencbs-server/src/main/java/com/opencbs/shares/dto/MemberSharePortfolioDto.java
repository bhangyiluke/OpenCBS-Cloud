package com.opencbs.shares.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MemberSharePortfolioDto {
    private Long profileId;
    private String profileName;
    private Long totalQuantity;
    private BigDecimal totalValue;
    private List<ShareLotDto> lots;
    private List<ShareTransactionDto> transactions;
}
