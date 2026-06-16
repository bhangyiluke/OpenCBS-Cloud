package com.opencbs.shares.repositories;

import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.core.repositories.Repository;
import com.opencbs.shares.domain.ShareLot;
import com.opencbs.shares.domain.enums.ShareLotStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShareLotRepository extends Repository<ShareLot> {

    @Query("select sl from ShareLot sl where sl.profile.id = :profileId and sl.status = :status and sl.availableQuantity > 0 order by sl.acquisitionDate asc, sl.id asc")
    List<ShareLot> findAvailableLotsByProfile(@Param("profileId") Long profileId, @Param("status") ShareLotStatus status);

    @Query("select sl from ShareLot sl where sl.id = :id and sl.status = :status and sl.availableQuantity > 0")
    Optional<ShareLot> findAvailableLotById(@Param("id") Long id, @Param("status") ShareLotStatus status);

    @Query("select sl from ShareLot sl where sl.profile.id = :profileId and (:productId is null or sl.shareProduct.id = :productId) and sl.status = :status and sl.availableQuantity > 0 order by sl.acquisitionDate asc, sl.id asc")
    Page<ShareLot> searchAvailableLotsByProfile(Pageable pageable, @Param("profileId") Long profileId, @Param("productId") Long productId, @Param("status") ShareLotStatus status);

    @Query("select coalesce(sum(sl.quantity), 0) from ShareLot sl where sl.profile.id = :profileId and (:productId is null or sl.shareProduct.id = :productId) and sl.status = :status")
    Long sumQuantityByProfileAndProduct(@Param("profileId") Long profileId, @Param("productId") Long productId, @Param("status") ShareLotStatus status);

    @Query("select coalesce(sum(sl.quantity), 0) from ShareLot sl where (:productId is null or sl.shareProduct.id = :productId) and (:branchId is null or sl.branch.id = :branchId) and sl.status = :status")
    Long sumQuantity(@Param("productId") Long productId, @Param("branchId") Long branchId, @Param("status") ShareLotStatus status);

    @Query("select sl from ShareLot sl where (:profileId is null or sl.profile.id = :profileId) and (:productId is null or sl.shareProduct.id = :productId) and (:branchId is null or sl.branch.id = :branchId) and sl.status = :status and sl.availableQuantity > 0 order by sl.acquisitionDate asc, sl.id asc")
    List<ShareLot> searchLots(@Param("profileId") Long profileId, @Param("productId") Long productId, @Param("branchId") Long branchId, @Param("status") ShareLotStatus status);

    List<ShareLot> findByProfileId(Long profileId);
}
