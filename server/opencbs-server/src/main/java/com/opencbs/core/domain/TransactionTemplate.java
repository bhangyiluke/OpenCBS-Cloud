package com.opencbs.core.domain;

import lombok.Data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "transaction_template")
public class TransactionTemplate extends NamedBaseEntity {

    @OneToMany(mappedBy = "transactionTemplate", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<TransactionTemplateAccounts> accounts;
}
