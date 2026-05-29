package com.opencbs.loans.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "loans")
public class LoanInterestAccrual extends LoanBaseEntity {

    @Column(name = "interest_rate")
    private BigDecimal interestRate;
}
