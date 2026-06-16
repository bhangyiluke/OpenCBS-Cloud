package com.opencbs.shares.dto;

import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.dto.BaseDto;
import com.opencbs.shares.domain.enums.ShareLotSelectionPolicy;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareProductDto extends BaseDto {
    private String name;
    private String code;
    private Long currencyId;
    private BigDecimal nominalValue;
    private BigDecimal unitPrice;
    private Long minSharesPerMember;
    private Long maxSharesPerMember;
    private Boolean allowMemberTransfers;
    private ShareLotSelectionPolicy lotSelectionPolicy = ShareLotSelectionPolicy.FIFO;
    private StatusType statusType = StatusType.ACTIVE;
}
