package com.opencbs.core.domain.trees;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "payment_methods")
public class PaymentMethod extends TreeEntity<PaymentMethod> {
}
