package com.opencbs.loans.domain.creditCommittee;

import com.opencbs.core.domain.CreationInfoEntity;
import com.opencbs.core.domain.Role;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "credit_committee_amount_range")
public class CreditCommitteeAmountRange extends CreationInfoEntity {

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @ManyToMany
    @JoinTable(
            name = "credit_committee_roles",
            joinColumns = @JoinColumn(name = "credit_committee_amount_range_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;
}