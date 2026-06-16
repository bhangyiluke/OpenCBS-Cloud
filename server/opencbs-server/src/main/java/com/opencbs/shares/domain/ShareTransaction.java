package com.opencbs.shares.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.Branch;
import com.opencbs.core.domain.User;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.shares.domain.enums.ShareTransactionStatus;
import com.opencbs.shares.domain.enums.ShareTransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Audited
@Table(name = "share_transactions")
@EqualsAndHashCode(callSuper = true)
public class ShareTransaction extends BaseEntity {

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_product_id", nullable = false)
    private ShareProduct shareProduct;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_profile_id")
    private Profile sourceProfile;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_profile_id")
    private Profile destinationProfile;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ShareTransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShareTransactionStatus status = ShareTransactionStatus.PENDING;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "idempotency_key", length = 128)
    private String idempotencyKey;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;
}
