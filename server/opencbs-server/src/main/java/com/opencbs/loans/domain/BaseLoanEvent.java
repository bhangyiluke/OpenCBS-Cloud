package com.opencbs.loans.domain;

import com.opencbs.core.accounting.domain.AccountingEntry;
import com.opencbs.core.domain.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MappedSuperclass;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"groupKey"}, callSuper = false)

@MappedSuperclass
public class BaseLoanEvent extends BaseEvent {

    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "loans_events_accounting_entries",
               joinColumns = @JoinColumn(name = "loan_event_id"),
               inverseJoinColumns = @JoinColumn(name = "accounting_entry_id"))
    private List<AccountingEntry> accountingEntry;

}
