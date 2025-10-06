package com.opencbs.core.domain;

import com.opencbs.core.accounting.domain.Account;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "payees")
@Data
public class Payee extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "payees_accounts",
            joinColumns = @JoinColumn(name = "payee_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> payeeAccounts;
}
