package com.opencbs.loans.domain.creditCommittee;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "Vote")
@Table(name = "credit_committee_votes")
public class CreditCommitteeVote extends CreditCommitteeVoteHistoryBaseEntity {

}
