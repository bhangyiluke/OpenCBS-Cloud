package com.opencbs.core.domain.customfields;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "branch_custom_fields_sections")
public class BranchCustomFieldSection extends CustomFieldSection<BranchCustomField> { }
