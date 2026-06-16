package com.opencbs.shares.repositories;

import com.opencbs.core.repositories.Repository;
import com.opencbs.shares.domain.ShareTransaction;
import com.opencbs.shares.domain.enums.ShareTransactionStatus;
import com.opencbs.shares.domain.enums.ShareTransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ShareTransactionRepository extends Repository<ShareTransaction> {

    Optional<ShareTransaction> findByIdempotencyKey(String idempotencyKey);

    @Query("select st from ShareTransaction st where (:profileId is null or st.sourceProfile.id = :profileId or st.destinationProfile.id = :profileId) and (:productId is null or st.shareProduct.id = :productId) and (:type is null or st.type = :type) and (:status is null or st.status = :status) and (:branchId is null or st.branch.id = :branchId) and (:startDate is null or st.transactionDate >= :startDate) and (:endDate is null or st.transactionDate <= :endDate) order by st.transactionDate desc, st.id desc")
    Page<ShareTransaction> search(Pageable pageable, @Param("profileId") Long profileId, @Param("productId") Long productId, @Param("type") ShareTransactionType type, @Param("status") ShareTransactionStatus status, @Param("branchId") Long branchId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
