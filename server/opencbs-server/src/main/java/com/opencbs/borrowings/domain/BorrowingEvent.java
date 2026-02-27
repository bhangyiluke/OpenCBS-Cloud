package com.opencbs.borrowings.domain;

import com.opencbs.core.accounting.domain.AccountingEntry;
import com.opencbs.core.domain.BaseEvent;
import com.opencbs.core.domain.json.ExtraJson;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "accountingEntry", callSuper = true)

@Entity
@Table(name = "borrowing_events")
public class BorrowingEvent extends BaseEvent {

    @Column(name = "borrowing_id", nullable = false)
    private Long borrowingId;

    // @Type(type = "ExtraJsonType")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extra", columnDefinition = "jsonb")
    private ExtraJson extra;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "borrowing_events_accounting_entries",
            joinColumns = @JoinColumn(name = "borrowing_event_id"),
            inverseJoinColumns = @JoinColumn(name = "accounting_entry_id"))
    private List<AccountingEntry> accountingEntry;
}
