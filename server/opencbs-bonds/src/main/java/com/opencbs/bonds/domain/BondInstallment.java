package com.opencbs.bonds.domain;

import com.opencbs.core.domain.BaseInstallment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "bonds_installments")
public class BondInstallment extends BaseInstallment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonds_id", nullable = false)
    private Bond bond;

    @Transient
    private BigDecimal accruedInterest = BigDecimal.ZERO;

    public BondInstallment(BondInstallment copy) {
        super(copy);
    }
}
