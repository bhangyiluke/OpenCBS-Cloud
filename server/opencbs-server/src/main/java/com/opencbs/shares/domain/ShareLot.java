package com.opencbs.shares.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.Branch;
import com.opencbs.core.domain.User;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.shares.domain.enums.ShareLotStatus;
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
@Table(name = "share_lots")
@EqualsAndHashCode(callSuper = true)
public class ShareLot extends BaseEntity {

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "share_product_id", nullable = false)
    private ShareProduct shareProduct;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "available_quantity", nullable = false)
    private Long availableQuantity;

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Column(name = "source_transaction_id")
    private Long sourceTransactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_transaction_type")
    private ShareTransactionType sourceTransactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShareLotStatus status = ShareLotStatus.ACTIVE;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;
}
