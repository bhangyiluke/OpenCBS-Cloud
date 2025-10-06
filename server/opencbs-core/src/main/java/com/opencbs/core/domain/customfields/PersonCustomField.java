package com.opencbs.core.domain.customfields;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Audited
@Table(name = "people_custom_fields")
@Where(clause = "deleted = false")
public class PersonCustomField extends CustomField<PersonCustomFieldSection> { }
