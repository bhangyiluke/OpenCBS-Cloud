package com.opencbs.loans.domain.customfields;

import com.opencbs.core.domain.customfields.CustomFieldValue;
import com.opencbs.loans.domain.Collateral;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "collaterals_custom_fields_values")
@Data
public class CollateralCustomFieldValue extends CustomFieldValue<TypeOfCollateralCustomField> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Collateral collateral;

    @Override
    public Long getOwnerId() {
        return collateral.getId();
    }
}