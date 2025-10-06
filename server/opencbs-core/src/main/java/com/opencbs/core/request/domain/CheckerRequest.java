package com.opencbs.core.request.domain;

import com.opencbs.core.domain.BaseEntity;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "checker_request")
public class CheckerRequest extends BaseEntity {

    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

}
