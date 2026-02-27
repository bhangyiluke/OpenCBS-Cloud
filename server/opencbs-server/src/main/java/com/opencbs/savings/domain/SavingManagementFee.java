package com.opencbs.savings.domain;

import com.opencbs.core.contracts.Contract;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Immutable
@Table(name = "savings")
public class SavingManagementFee extends Contract {

    @Column(name = "management_fee_rate")
    private BigDecimal managementFeeRate;

    @Column(name = "management_fee_flat")
    private BigDecimal managementFeeFlat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "saving_product_id")
    private SavingProductSimple product;

    @Column(name = "profile_id")
    private Long profileId;
}
