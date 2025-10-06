package com.opencbs.termdeposite.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.accounting.domain.Account;
import com.opencbs.termdeposite.domain.enums.TermDepositAccountType;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Table(name = "term_deposit_product_accounts")
@EqualsAndHashCode(callSuper = true)
public class TermDepositProductAccount extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TermDepositAccountType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_deposit_product_id", nullable = false)
    private TermDepositProduct product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}
