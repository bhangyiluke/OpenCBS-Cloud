package com.opencbs.core.domain;

import com.opencbs.core.domain.customfields.BranchCustomFieldValue;
import lombok.Data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Data
@Table(name = "branches")
public class Branch extends NamedBaseEntity {

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<BranchCustomFieldValue> customFieldValues;

    @Override
    public String toString() {
        return this.getName();
    }
}
