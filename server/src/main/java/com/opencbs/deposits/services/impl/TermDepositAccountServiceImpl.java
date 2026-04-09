package com.opencbs.deposits.services.impl;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.deposits.domain.TermDepositAccount;
import com.opencbs.deposits.domain.enums.TermDepositAccountType;
import com.opencbs.deposits.repositories.TermDepositAccountRepository;
import com.opencbs.deposits.services.TermDepositAccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TermDepositAccountServiceImpl implements TermDepositAccountService {

    private final TermDepositAccountRepository termDepositAccountRepository;


    @Override
    public Account getTermDepositAccount(@NonNull Long terDepositId, @NonNull TermDepositAccountType type) {
        Optional<TermDepositAccount> termDepositAccount = termDepositAccountRepository.findByTermDepositIdAndType(terDepositId, type);
        if (termDepositAccount.isPresent()) {
            return termDepositAccount.get().getAccount();
        }

        throw new RuntimeException(String.format("Account is not found by term deposit rule(%s).", type.toString()));
    }
}
