package com.opencbs.core.domain;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.domain.enums.PenaltyType;
import lombok.Data;

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
@Table(name = "penalties")
public class Penalty extends NamedBaseEntity {

    @Column(name = "begin_period_day", nullable = false)
    private Integer beginPeriodDay;

    @Column(name = "end_period_day", nullable = false)
    private Integer endPeriodDay;

    @Column(name = "grace_period",nullable = false)
    private Integer gracePeriod = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "penalty_type", nullable = false)
    private PenaltyType penaltyType;

    @Column(name = "penalty", nullable = false)
    private BigDecimal penalty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accrual_account_id", nullable = false)
    private Account accrualAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_account_id", nullable = false)
    private Account incomeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "write_off_account_id", nullable = false)
    private Account writeOffAccount;

    @Override
    public String toString() {
        return this.getName();
    }
}
