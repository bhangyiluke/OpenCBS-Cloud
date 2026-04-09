package com.opencbs.deposits.processing;

import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.ProcessType;
import com.opencbs.deposits.annotaions.CustomTermDepositDayClosureProcessor;
import com.opencbs.deposits.domain.TermDepositClose;
import com.opencbs.deposits.repositories.TermDepositCloseRepository;
import com.opencbs.deposits.services.TermDepositCloseInterface;
import com.opencbs.deposits.services.TermDepositService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@ConditionalOnMissingBean(annotation = CustomTermDepositDayClosureProcessor.class)
public class TermDepositCloseProcessor implements TermDepositDayClosureProcessor {

    private final TermDepositService termDepositService;
    private final TermDepositCloseInterface termDepositCloseInterface;
    private final TermDepositCloseRepository termDepositCloseRepository;


    @Override
    public void processContract(@NonNull  Long termDepositId, @NonNull LocalDate date, @NonNull User user) {
        TermDepositClose termDeposit = termDepositCloseRepository.findById(termDepositId).orElseThrow(() ->
                new IllegalArgumentException("Term Deposit with id:: " + termDepositId + " not found"));
        LocalDate closeDate = termDepositService.getExpiredDate(termDeposit.getOpenDate(), termDeposit.getTermAgreement());
        if (date.equals(closeDate)) {
            termDepositCloseInterface.close(termDepositId, date);
        }
    }

    @Override
    public ProcessType getProcessType() {
        return ProcessType.TERM_DEPOSIT_CLOSING;
    }

    @Override
    public String getIdentityString() {
        return "term-deposit.closing";
    }
}
