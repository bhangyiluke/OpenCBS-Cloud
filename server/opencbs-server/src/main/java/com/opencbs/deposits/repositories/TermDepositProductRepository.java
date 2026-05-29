package com.opencbs.deposits.repositories;

import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.repositories.Repository;
import com.opencbs.deposits.domain.TermDepositProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;
import java.util.Optional;

public interface TermDepositProductRepository extends RevisionRepository<TermDepositProduct, Long, Integer>, Repository<TermDepositProduct> {

    Page<TermDepositProduct> findAllByNameIgnoreCaseContainingAndStatusTypeIn(Pageable pageable, String searchString, List<StatusType> statusTypes);

    Optional<TermDepositProduct> findByCode(String code);

    Optional<TermDepositProduct> findByName(String name);

}