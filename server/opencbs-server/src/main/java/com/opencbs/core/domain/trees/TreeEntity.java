package com.opencbs.core.domain.trees;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencbs.core.domain.BaseEntity;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class TreeEntity<T extends TreeEntity> extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    protected String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    protected T parent;
}
