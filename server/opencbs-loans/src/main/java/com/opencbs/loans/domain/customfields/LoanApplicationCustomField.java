package com.opencbs.loans.domain.customfields;

import com.opencbs.core.domain.customfields.CustomField;
import lombok.Data;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "loan_application_custom_fields")
@Where(clause = "deleted = false")
public class LoanApplicationCustomField extends CustomField<LoanApplicationCustomFieldSection> {
}
