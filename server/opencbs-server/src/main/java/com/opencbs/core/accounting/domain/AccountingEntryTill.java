package com.opencbs.core.accounting.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.enums.TillOperation;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.core.domain.till.Till;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "accounting_entries_tills")
public class AccountingEntryTill extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accounting_entries_id", nullable = false)
    private AccountingEntry accountingEntries;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private TillOperation operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiated_by")
    private Profile initiatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "till_id", nullable = false)
    private Till till;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "initiator")
    private String initiator;
}
