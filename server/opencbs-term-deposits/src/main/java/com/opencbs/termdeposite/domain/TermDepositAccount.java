package com.opencbs.termdeposite.domain;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.domain.BaseEntity;
import com.opencbs.termdeposite.domain.enums.TermDepositAccountType;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "term_deposit_accounts")
public class TermDepositAccount extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TermDepositAccountType type;

    @Column(name = "term_deposit_id", insertable = false, updatable = false)
    private Long termDepositId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_deposit_id", nullable = false)
    private TermDeposit termDeposit;

    @Column(name = "account_id", insertable = false, updatable = false)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}
