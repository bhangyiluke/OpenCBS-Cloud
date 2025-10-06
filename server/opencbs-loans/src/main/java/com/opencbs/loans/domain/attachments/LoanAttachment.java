package com.opencbs.loans.domain.attachments;

import com.opencbs.core.domain.attachments.Attachment;
import com.opencbs.loans.domain.Loan;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Data
@Table(name = "loan_attachments")
public class LoanAttachment extends Attachment<Loan>{
}