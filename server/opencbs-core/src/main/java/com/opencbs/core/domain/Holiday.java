package com.opencbs.core.domain;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "holidays")
public class Holiday extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "is_annual", nullable = false)
    private boolean isAnnual;
}
