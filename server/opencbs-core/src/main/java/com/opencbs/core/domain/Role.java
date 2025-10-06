package com.opencbs.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencbs.core.domain.enums.StatusType;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Data
@Audited
@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements NamedEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Audited(targetAuditMode = NOT_AUDITED)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @JsonIgnore
    private Set<Permission> permissions;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private StatusType statusType = StatusType.ACTIVE;

    @NotAudited
    @Column(name="is_system", nullable = false)
    private Boolean isSystem = Boolean.FALSE;

    @Override
    public String toString() {
        return name;
    }
}

