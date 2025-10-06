package com.opencbs.core.request.domain;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.User;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;


@Data
@Entity
@Immutable
@Table(name = "request")
public class ActionRequest extends BaseEntity {

    @Column(name = "approved_at")
    private LocalDateTime actionAt;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private User approvedBy;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Transient
    private RequestActionType requestActionType;
}
