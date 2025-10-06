package com.opencbs.loans.domain.customfields;

import com.opencbs.core.domain.customfields.CustomFieldSection;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "types_of_collateral")
public class TypeOfCollateral extends CustomFieldSection<TypeOfCollateralCustomField> {
}
