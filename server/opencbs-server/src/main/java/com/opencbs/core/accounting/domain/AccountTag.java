package com.opencbs.core.accounting.domain;

import com.opencbs.core.domain.BaseEntity;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "account_tags")
public class AccountTag extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private AccountTagType name;
}