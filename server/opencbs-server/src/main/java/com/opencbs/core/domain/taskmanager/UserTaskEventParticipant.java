package com.opencbs.core.domain.taskmanager;

import com.opencbs.core.domain.User;
import lombok.Data;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@DiscriminatorValue("User")
public class UserTaskEventParticipant extends TaskEventParticipant {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id", nullable = false)
    private User user;
}
