package com.opencbs.loans.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.EntryFee;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@Table(name="loan_applications_entry_fees")
public class LoanApplicationEntryFee extends BaseEntity {
    @Column(name="amount", precision = 12, scale = 4)
    private BigDecimal amount;

    @Column(name="rate", precision = 12, scale = 4)
    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loan_application_id")
    private LoanApplication loanApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="entry_fee_id")
    private EntryFee entryFee;
}
