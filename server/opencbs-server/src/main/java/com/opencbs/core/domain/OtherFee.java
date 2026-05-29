package com.opencbs.core.domain;

import com.opencbs.core.accounting.domain.Account;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "other_fees")
public class OtherFee extends CreationInfoEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "charge_account_id", nullable = false)
    private Account chargeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_account_id", nullable = false)
    private Account incomeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_account_id", nullable = false)
    private Account expenseAccount;
}
