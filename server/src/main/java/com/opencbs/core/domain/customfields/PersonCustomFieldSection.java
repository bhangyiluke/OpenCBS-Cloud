package com.opencbs.core.domain.customfields;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "people_custom_fields_sections")
public class PersonCustomFieldSection extends CustomFieldSection<PersonCustomField> { }