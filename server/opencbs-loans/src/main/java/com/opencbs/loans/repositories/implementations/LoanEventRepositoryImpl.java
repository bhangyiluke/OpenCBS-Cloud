package com.opencbs.loans.repositories.implementations;

import com.opencbs.core.domain.enums.EventType;
import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.loans.domain.LoanEvent;
import com.opencbs.loans.repositories.customs.LoanEventRepositoryCustom;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class LoanEventRepositoryImpl extends BaseRepository<LoanEvent> implements LoanEventRepositoryCustom {

    //@Autowired
    public LoanEventRepositoryImpl(EntityManager entityManager) {
        super(entityManager, LoanEvent.class);
    }

    @Override
    public List<LoanEvent> findAllByLoanIdAndDeletedAndEventTypeAndEffectiveAt(Long loanId, boolean isDeleted, EventType eventType, LocalDateTime localDateTime) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<LoanEvent> cq = cb.createQuery(LoanEvent.class);
        Root<LoanEvent> root = cq.from(LoanEvent.class);
        Predicate predicate = cb.conjunction();
        predicate = cb.and(predicate, cb.equal(root.get("loanId"), loanId));
        predicate = cb.and(predicate, cb.equal(root.get("deleted"), isDeleted));
        predicate = cb.and(predicate, cb.equal(root.get("eventType"), eventType));
        if (localDateTime != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("effectiveAt"), localDateTime));
        }
        cq.select(root).where(predicate).distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public List<LoanEvent> findAllByLoanIdAndEffectiveAt(Long loanId, LocalDateTime from, LocalDateTime to) {
        String sql = " select l from LoanEvent l " +
                "where l.effectiveAt between :from and :to " +
                "and (l.loanId = :loanId)" +
                "and (l.deleted = false)";

        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("loanId", loanId);
        return query.getResultList();
    }



    @Override
    public List<LoanEvent> findAllByLoanIdAndDeletedAndEventTypeAndEffectiveAtBetween(Long loanId,boolean isDeleted, EventType eventType, LocalDateTime startPeriod, LocalDateTime endPeriod) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<LoanEvent> cq = cb.createQuery(LoanEvent.class);
        Root<LoanEvent> root = cq.from(LoanEvent.class);

        Predicate predicate = cb.conjunction();
        predicate = cb.and(predicate, cb.equal(root.get("loanId"), loanId));
        predicate = cb.and(predicate, cb.equal(root.get("deleted"), isDeleted));
        predicate = cb.and(predicate, cb.equal(root.get("eventType"), eventType));
        if (startPeriod != null && endPeriod != null) {
            predicate = cb.and(predicate, cb.between(root.get("effectiveAt"), startPeriod, endPeriod));
        }

        cq.select(root).where(predicate).distinct(true);
        return getEntityManager().createQuery(cq).getResultList();
    }
}
