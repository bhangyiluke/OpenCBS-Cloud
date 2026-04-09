package com.opencbs.borrowings.domain;

import com.opencbs.core.domain.BaseProduct;
import lombok.Data;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "borrowing_products")
public class BorrowingProduct extends BaseProduct {

    @OneToMany(mappedBy = "borrowingProduct", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<BorrowingProductAccount> accounts;
}