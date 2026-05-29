package com.opencbs.loans.domain.schedules.installments;

import com.opencbs.core.domain.schedule.installments.ScheduleInstallment;
import com.opencbs.loans.domain.LoanApplication;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "loan_applications_installments")
public class LoanApplicationInstallment extends ScheduleInstallment {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;
}
