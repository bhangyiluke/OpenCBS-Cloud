package com.opencbs.core.domain;

import com.opencbs.core.domain.enums.CustomFieldType;
import com.opencbs.core.domain.enums.SystemSettingsName;
import com.opencbs.core.domain.enums.SystemSettingsSections;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "system_settings")
public class SystemSettings extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private SystemSettingsName name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CustomFieldType type;

    @Column(name = "value", nullable = false)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false)
    private SystemSettingsSections section;

}
