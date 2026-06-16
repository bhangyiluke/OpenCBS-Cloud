package com.opencbs.shares.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.Currency;
import com.opencbs.core.domain.NamedEntity;
import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.shares.domain.enums.ShareLotSelectionPolicy;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Audited
@Table(name = "share_products")
@EqualsAndHashCode(callSuper = true)
public class ShareProduct extends BaseEntity implements NamedEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "nominal_value", nullable = false, precision = 19, scale = 4)
    private BigDecimal nominalValue;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "min_shares_per_member", nullable = false)
    private Long minSharesPerMember;

    @Column(name = "max_shares_per_member")
    private Long maxSharesPerMember;

    @Column(name = "allow_member_transfers", nullable = false)
    private Boolean allowMemberTransfers = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "lot_selection_policy", nullable = false)
    private ShareLotSelectionPolicy lotSelectionPolicy = ShareLotSelectionPolicy.FIFO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusType statusType = StatusType.ACTIVE;

    @Override
    public String toString() {
        return this.name;
    }
}
