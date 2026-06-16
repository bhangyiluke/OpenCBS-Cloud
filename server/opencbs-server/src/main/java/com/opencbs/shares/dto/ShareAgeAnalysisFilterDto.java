package com.opencbs.shares.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShareAgeAnalysisFilterDto {
    private Long profileId;
    private Long shareProductId;
    private Long branchId;
    private LocalDate reportDate;
}
