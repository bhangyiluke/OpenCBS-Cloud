package com.opencbs.core.domain;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "profiles_accounts")
public class ProfileAccounts extends BaseEntity {

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "account_id", nullable = false)
    private Long accountId;
}
