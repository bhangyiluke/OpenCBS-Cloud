package com.opencbs.core.domain.customfields;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Audited
@Table(name = "companies_custom_fields")
@SQLRestriction("deleted = false")
public class CompanyCustomField extends CustomField<CompanyCustomFieldSection> {
}
