package com.opencbs.loans.domain.creditCommittee;

import com.opencbs.loans.domain.enums.LoanApplicationStatus;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity(name = "history")
@Data
@Table(name = "credit_committee_votes_history")
public class CreditCommitteeVoteHistory extends CreditCommitteeVoteHistoryBaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private LoanApplicationStatus oldStatus;
}

