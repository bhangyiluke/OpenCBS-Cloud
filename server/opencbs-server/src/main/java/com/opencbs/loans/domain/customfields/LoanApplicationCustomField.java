package com.opencbs.loans.domain.customfields;

import com.opencbs.core.domain.customfields.CustomField;
import lombok.Data;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "loan_application_custom_fields")
@SQLRestriction("deleted = false")
public class LoanApplicationCustomField extends CustomField<LoanApplicationCustomFieldSection> {
}
