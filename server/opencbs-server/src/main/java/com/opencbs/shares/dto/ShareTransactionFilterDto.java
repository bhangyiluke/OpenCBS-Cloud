package com.opencbs.shares.dto;

import com.opencbs.shares.domain.enums.ShareTransactionStatus;
import com.opencbs.shares.domain.enums.ShareTransactionType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ShareTransactionFilterDto {
    private Long profileId;
    private Long shareProductId;
    private ShareTransactionType type;
    private ShareTransactionStatus status;
    private Long branchId;
    private LocalDate startDate;
    private LocalDate endDate;
}
