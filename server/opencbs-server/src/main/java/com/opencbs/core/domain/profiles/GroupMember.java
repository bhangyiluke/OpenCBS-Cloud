package com.opencbs.core.domain.profiles;

import com.opencbs.core.domain.BaseEntity;
import lombok.Data;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Data
@Audited
@Entity
@Table(name = "groups_members")
public class GroupMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Person member;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;

    @Column(name = "left_date")
    private LocalDateTime leftDate;

}