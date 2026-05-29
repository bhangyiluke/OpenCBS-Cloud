package com.opencbs.core.domain.customfields;

import com.opencbs.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper=false)
public class CustomFieldSection<Tcf extends CustomField> extends BaseEntity {

    public CustomFieldSection() {
        this.customFields = new ArrayList<>();
    }

    @Column(name = "caption", nullable = false)
    private String caption;

    @Column(name = "[order]", nullable = false)
    private int order;

    @OrderBy("order asc")
    @OneToMany(mappedBy = "section", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @SQLRestriction("deleted = false")
    List<Tcf> customFields;

    @Column(name = "code", nullable = false)
    private String code;
}
