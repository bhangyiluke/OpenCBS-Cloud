package com.opencbs.core.domain.till;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.Branch;
import com.opencbs.core.domain.User;
import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.domain.enums.TillStatus;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "tills")
public class Till extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TillStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_changed_by_id", nullable = false)
    private User lastChangedBy;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @ManyToMany
    @JoinTable(name = "tills_accounts",
            joinColumns = @JoinColumn(name = "till_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> accounts;

    @OneToMany(mappedBy = "till")
    private List<TillEvent> events;

    public User getTeller() {
        if (status.equals(TillStatus.OPENED)) {
            return this.getEvents()
                    .stream()
                    .max(Comparator.comparing(TillEvent::getCreatedAt))
                    .map(TillEvent::getTeller)
                    .orElse(null);
        }
        else return null;
    }
}
