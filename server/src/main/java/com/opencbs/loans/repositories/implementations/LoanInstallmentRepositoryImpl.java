package com.opencbs.loans.repositories.implementations;

import com.google.common.collect.ImmutableList;
import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.loans.domain.LoanInstallment;
import com.opencbs.loans.repositories.customs.LoanInstallmentRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class LoanInstallmentRepositoryImpl extends BaseRepository<LoanInstallment> implements LoanInstallmentRepositoryCustom {

    //@Autowired
    public LoanInstallmentRepositoryImpl(EntityManager entityManager) {
        super(entityManager, LoanInstallment.class);
    }

    @Override
    public ImmutableList<LoanInstallment> findByLoanId(Long loanId) {
        return findByLoanIdAndDateTime(loanId, null);
    }

    @Override
    public ImmutableList<LoanInstallment> findByLoanIdAndDateTime(Long loanId, LocalDateTime timestamp) {
        String queryString;
        if (timestamp == null) {
            queryString = "select * from get_loan_schedule(:loanId, null)";
        } else {
            queryString = "select * from get_loan_schedule(:loanId, :dateTime)";
        }
        Query query = this.getEntityManager().createNativeQuery(queryString, LoanInstallment.class);
        query.setParameter("loanId", loanId);
        if (timestamp != null) {
            query.setParameter("dateTime", timestamp);
        }

        return ImmutableList.<LoanInstallment>builder()
                .addAll(query.getResultList())
                .build();
    }

    @Override
    public ImmutableList<LoanInstallment> findAllByLoanIdAndEffectiveAtAndDeleted(Long loanId, LocalDateTime localDateTime, boolean deleted) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<LoanInstallment> cq = cb.createQuery(LoanInstallment.class);
        Root<LoanInstallment> root = cq.from(LoanInstallment.class);
        List<Predicate> preds = new ArrayList<>();
        preds.add(cb.equal(root.get("loanId"), loanId));
        preds.add(cb.equal(root.get("deleted"), deleted));
        if (localDateTime != null) {
            preds.add(cb.lessThanOrEqualTo(root.get("effectiveAt"), localDateTime));
        }
        cq.select(root).where(preds.toArray(new Predicate[0]));
        cq.distinct(true);

        TypedQuery<LoanInstallment> q = getEntityManager().createQuery(cq);
        List<LoanInstallment> list = q.getResultList();

        return ImmutableList.<LoanInstallment>builder()
                .addAll(list)
                .build();
    }
}
