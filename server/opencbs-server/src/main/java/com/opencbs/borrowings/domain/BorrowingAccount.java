package com.opencbs.borrowings.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.accounting.domain.Account;
import com.opencbs.borrowings.domain.enums.BorrowingRuleType;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "borrowing_accounts")
public class BorrowingAccount extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BorrowingRuleType accountRuleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowing_id", nullable = false)
    private Borrowing borrowing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}