package com.opencbs.savings.domain;

import com.opencbs.core.contracts.Contract;
import com.opencbs.core.domain.enums.Frequency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Immutable
@Table(name = "savings")
public class SavingPosting extends Contract {

    @Column(name = "code")
    private String code;

    @Column(name = "capitalized")
    private boolean capitalized;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "saving_product_id")
    private SavingProductSimple product;

    @Column(name = "profile_id")
    private Long profileId;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_accrual_frequency")
    private Frequency interestAccrualFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_posting_frequency", nullable = false)
    private Frequency interestPostingFrequency;

    @Column(name = "open_date")
    private LocalDateTime openDate;

    @Column(name = "interest_rate", nullable = false)
    private BigDecimal interestRate;
}
