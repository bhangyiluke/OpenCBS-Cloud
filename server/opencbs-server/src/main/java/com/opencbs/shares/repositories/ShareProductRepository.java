package com.opencbs.shares.repositories;

import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.repositories.Repository;
import com.opencbs.shares.domain.ShareProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShareProductRepository extends RevisionRepository<ShareProduct, Long, Integer>, Repository<ShareProduct> {

    Optional<ShareProduct> findByCode(String code);

    Optional<ShareProduct> findByName(String name);

    @Query("select p from ShareProduct p where (:search is null or lower(p.name) like lower(concat('%', :search, '%')) or lower(p.code) like lower(concat('%', :search, '%'))) and (:statusType is null or p.statusType = :statusType) order by p.id")
    Page<ShareProduct> search(Pageable pageable, @Param("search") String search, @Param("statusType") StatusType statusType);
}
