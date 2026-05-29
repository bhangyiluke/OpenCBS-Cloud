package com.opencbs.deposits.services;

import com.opencbs.deposits.domain.TermDeposit;

import javax.script.ScriptException;

public interface TermDepositCodeGenerator {

    String generateCode(TermDeposit termDeposit) throws ScriptException;
}
