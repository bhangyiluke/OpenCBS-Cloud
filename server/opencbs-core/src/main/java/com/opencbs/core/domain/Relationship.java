package com.opencbs.core.domain;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "relationships")
public class Relationship extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
}
