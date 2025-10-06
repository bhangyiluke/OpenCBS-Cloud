package com.opencbs.loans.domain.trees;

import com.opencbs.core.domain.trees.TreeEntity;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "loan_purposes")
public class LoanPurpose extends TreeEntity<LoanPurpose> {
}
