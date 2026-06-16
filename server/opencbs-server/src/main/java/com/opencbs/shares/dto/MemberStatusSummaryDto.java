package com.opencbs.shares.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberStatusSummaryDto {
    private String status;
    private Long quantity;
    private BigDecimal value;
}
