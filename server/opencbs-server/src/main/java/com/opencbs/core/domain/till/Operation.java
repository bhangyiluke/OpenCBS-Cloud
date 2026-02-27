package com.opencbs.core.domain.till;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.Currency;
import com.opencbs.core.domain.User;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "view_operation")
public class Operation extends BaseEntity {

    @Column(name = "effective_at")
    private LocalDateTime effectiveAt;

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "vault_name")
    private String vaultName;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "operation_type")
    private String operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "description")
    private String description;

    @Column(name = "till_id")
    private Long tillId;
}
