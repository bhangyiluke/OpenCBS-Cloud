package com.opencbs.deposits.services;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.deposits.domain.TermDeposit;
import com.opencbs.deposits.domain.TermDepositAccount;
import com.opencbs.deposits.domain.enums.TermDepositAccountType;

import java.util.List;

public interface TermDepositAccountingService {

    List<TermDepositAccount> createAccounts(TermDeposit termDeposit);
    Account getAccountByType(List<TermDepositAccount> accounts, TermDepositAccountType type);
}
