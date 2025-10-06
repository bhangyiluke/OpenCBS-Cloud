package com.opencbs.core.domain.trees;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "business_sectors")
public class BusinessSector extends TreeEntity<BusinessSector> {
}
