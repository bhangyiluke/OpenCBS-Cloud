package com.opencbs.core.domain;

import com.opencbs.core.domain.enums.ModuleType;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "module_type", nullable = false)
    private ModuleType moduleType;

    @Column(name = "permanent", nullable = false)
    private Boolean permanent;

    @Override
    public String toString() {
        return name;
    }
}
