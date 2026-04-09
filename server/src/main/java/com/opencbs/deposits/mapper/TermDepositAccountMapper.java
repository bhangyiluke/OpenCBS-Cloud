package com.opencbs.deposits.mapper;

import com.opencbs.core.annotations.Mapper;
import com.opencbs.core.accounting.mappers.AccountMapper;
import com.opencbs.deposits.domain.TermDepositAccount;
import com.opencbs.deposits.dto.TermDepositAccountDetailsDto;
import lombok.NonNull;

import java.util.List;


@Mapper
public class TermDepositAccountMapper {

    private final AccountMapper accountMapper;

    public TermDepositAccountMapper(@NonNull AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    public TermDepositAccountDetailsDto mapToDto(List<TermDepositAccount> accounts) {
        if (accounts == null) {
            return null;
        }
        TermDepositAccountDetailsDto accountDetailsDto = new TermDepositAccountDetailsDto();
        accounts.forEach(x ->
                accountDetailsDto.put(x.getType(), this.accountMapper.mapToDetailsDto(x.getAccount())));

        return accountDetailsDto;
    }
}