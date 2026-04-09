package com.opencbs.deposits.services;

import com.opencbs.deposits.domain.TermDeposit;
import com.opencbs.deposits.domain.TermDepositAccountingEntry;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermDepositAccountingEntryService {

    Page<TermDepositAccountingEntry> getPageableByTermDeposit(@NonNull TermDeposit termDeposit, @NonNull Pageable pageable);

    void save(@NonNull TermDepositAccountingEntry entry);
}
