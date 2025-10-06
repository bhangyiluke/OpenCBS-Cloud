package com.opencbs.core.domain.profiles;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.domain.Branch;
import com.opencbs.core.domain.CreationInfoEntity;
import com.opencbs.core.domain.NamedEntity;
import com.opencbs.core.domain.enums.EntityStatus;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Audited
@Table(name = "profiles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "[type]", discriminatorType = DiscriminatorType.STRING)
public class Profile extends CreationInfoEntity implements NamedEntity {

    @Column(name = "[type]", nullable = false, insertable = false, updatable = false)
    private String type;

    @Column(name = "[name]", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EntityStatus status;

    @Column(name = "version")
    private Integer version;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @NotAudited
    @ManyToMany
    @JoinTable(name = "profiles_accounts",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> currentAccounts = new HashSet<>();

    public String getNameFromCustomFields() {
        return null;
    }
}
