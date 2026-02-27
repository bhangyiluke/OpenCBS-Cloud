package com.opencbs.deposits.repositories;

import com.opencbs.core.repositories.Repository;
import com.opencbs.deposits.domain.TermDeposit;
import com.opencbs.deposits.domain.TermDepositAccountingEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermDepositAccountingEntryRepository extends Repository<TermDepositAccountingEntry> {

    Page<TermDepositAccountingEntry> findByTermDepositOrderByAccountingEntry_EffectiveAt(TermDeposit termDeposit, Pageable pageable);
}
