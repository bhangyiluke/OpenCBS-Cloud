package com.opencbs.borrowings.domain;

import com.opencbs.borrowings.domain.enums.BorrowingRuleType;
import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Immutable
@Table(name = "borrowing_accounts")
public class BorrowingAccountGainLoss extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BorrowingRuleType accountRuleType;

    @Column(name = "borrowing_id")
    private Long borrowingId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}