package com.opencbs.shares.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareAgeBucketDto {
    private String label;
    private Long quantity;
    private BigDecimal value;
}
