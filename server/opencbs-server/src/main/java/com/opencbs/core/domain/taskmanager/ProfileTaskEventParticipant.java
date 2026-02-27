package com.opencbs.core.domain.taskmanager;

import com.opencbs.core.domain.profiles.Profile;
import lombok.Data;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@DiscriminatorValue("Profile")
public class ProfileTaskEventParticipant extends TaskEventParticipant {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_id", nullable = false)
    private Profile profile;
}
