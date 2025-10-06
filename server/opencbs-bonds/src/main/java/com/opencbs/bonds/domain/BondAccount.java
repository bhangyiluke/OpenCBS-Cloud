package com.opencbs.bonds.domain;

import com.opencbs.bonds.domain.enums.BondAccountRuleType;
import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.accounting.domain.Account;
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
@Table(name = "bonds_accounts")
public class BondAccount extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private BondAccountRuleType bondAccountRuleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bond_id")
    private Bond bond;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
