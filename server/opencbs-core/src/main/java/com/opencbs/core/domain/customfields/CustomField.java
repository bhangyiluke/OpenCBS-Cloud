package com.opencbs.core.domain.customfields;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.enums.CustomFieldType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@Data

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SQLRestriction(value = "deleted = false")
@EqualsAndHashCode(callSuper = true)
public abstract class CustomField<Tcfs extends CustomFieldSection> extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    Tcfs section;

    @Column(name = "field_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CustomFieldType fieldType;

    @Column(name = "[name]", nullable = false, unique = true)
    private String name;

    @Column(name = "caption", nullable = false)
    private String caption;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_unique", nullable = false)
    private boolean unique;

    @Column(name = "is_required", nullable = false)
    private boolean required;

    @Column(name = "[order]", nullable = false)
    private int order;

    @Column(name = "extra")
    private CustomFieldExtra extra;

    @Column(name = "deleted")
    private Boolean deleted = false;
}
