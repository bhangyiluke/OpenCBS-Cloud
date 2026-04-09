package com.opencbs.core.contracts;

import com.opencbs.core.domain.BaseEntity;
import com.opencbs.core.domain.Branch;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@MappedSuperclass
public abstract class Contract extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;


    public Contract(@NonNull Long id) {
        super(id);
    }
}
