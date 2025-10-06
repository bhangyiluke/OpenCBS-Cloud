package com.opencbs.loans.domain.customfields;

import com.opencbs.core.domain.customfields.CustomFieldValue;
import com.opencbs.loans.domain.LoanApplication;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "loan_application_custom_fields_values")
public class LoanApplicationCustomFieldValue extends CustomFieldValue<LoanApplicationCustomField> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private LoanApplication owner;

    @Override
    public Long getOwnerId() {
        return owner.getId();
    }
}
