package com.opencbs.deposits.repositories;

import com.opencbs.deposits.dto.TermDepositSimplified;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermDepositRepositoryCustom {
    Page<TermDepositSimplified> getAllWithSearch(String searchString, Pageable pageable);
}