package com.opencbs.savings.domain;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "savings_accounts")
public class SavingAccount extends BaseSavingAccount {

    @Column(name = "saving_id", insertable = false, updatable = false)
    private Long savingId;

    @ManyToOne
    @JoinColumn(name = "saving_id", nullable = false)
    private Saving saving;
}
