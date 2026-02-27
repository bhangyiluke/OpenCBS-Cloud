package com.opencbs.deposits.services;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.deposits.domain.enums.TermDepositAccountType;

public interface TermDepositAccountService {

    Account getTermDepositAccount(Long termDepositId, TermDepositAccountType type);
}
