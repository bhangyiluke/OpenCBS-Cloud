package com.opencbs.core.domain.customfields;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Audited
@Table(name = "branch_custom_fields")
@SQLRestriction("deleted = false")
public class BranchCustomField extends CustomField<BranchCustomFieldSection> { }
