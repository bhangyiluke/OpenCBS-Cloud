package com.opencbs.loans.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.loans.domain.products.LoanProduct;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Data

@Entity
@Table(name = "loan_product_provisions")
public class LoanProductProvision extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_product_id", nullable = false)
    private LoanProduct loanProduct;

    @Column(name = "late_of_days")
    private Long lateOfDays;

    @Column(name = "provision_by_principal")
    private BigDecimal provisionByPrincipal;

    @Column(name = "provision_by_interest")
    private BigDecimal provisionByInterest;

    @Column(name = "provision_by_penalty")
    private BigDecimal provisionByPenalty;
}