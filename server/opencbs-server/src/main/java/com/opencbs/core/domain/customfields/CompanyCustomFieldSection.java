package com.opencbs.core.domain.customfields;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "companies_custom_fields_sections")
public class CompanyCustomFieldSection extends CustomFieldSection<CompanyCustomField> { }
