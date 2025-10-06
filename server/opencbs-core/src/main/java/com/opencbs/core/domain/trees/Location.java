package com.opencbs.core.domain.trees;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "locations")
public class Location extends TreeEntity<Location> {
}
