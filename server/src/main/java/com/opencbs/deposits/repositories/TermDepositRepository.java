package com.opencbs.deposits.repositories;

import com.opencbs.core.domain.Branch;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.core.repositories.Repository;
import com.opencbs.deposits.domain.TermDeposit;
import com.opencbs.deposits.domain.enums.TermDepositStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TermDepositRepository extends Repository<TermDeposit>, TermDepositRepositoryCustom {

    Page<TermDeposit> findByProfile(Pageable pageable, Profile profile);

    List<TermDeposit> findAllByProfileId(Long profileId);

    @Query("select td.id as id from TermDeposit td where td.status = ?1 and td.branch = ?2")
    List<Long> findIdsWhenTermDepositHasStatus(TermDepositStatus termDepositStatus, Branch branch);
}
