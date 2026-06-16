package com.opencbs.shares.dto;

import com.opencbs.core.domain.enums.StatusType;
import lombok.Data;

@Data
public class ShareProductListFilterDto {
    private String search;
    private StatusType statusType;
}
