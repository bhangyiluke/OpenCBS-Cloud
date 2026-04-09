package com.opencbs.core.domain.till;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.TillEventType;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "till_events")
public class TillEvent extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private TillEventType eventType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "till_id")
    private Till till;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teller_id")
    private User teller;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "comment")
    private String comment;
}
