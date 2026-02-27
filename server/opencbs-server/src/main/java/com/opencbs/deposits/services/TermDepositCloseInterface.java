package com.opencbs.deposits.services;

import com.opencbs.deposits.domain.TermDeposit;

import java.time.LocalDate;

public interface TermDepositCloseInterface {

    TermDeposit close(Long termDepositId, LocalDate closeDate);
}
