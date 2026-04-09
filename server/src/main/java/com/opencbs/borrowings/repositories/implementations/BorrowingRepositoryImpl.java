package com.opencbs.borrowings.repositories.implementations;

import com.opencbs.core.domain.enums.EventType;
import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.borrowings.domain.Borrowing;
import com.opencbs.borrowings.domain.BorrowingEvent;
import com.opencbs.borrowings.domain.SimplifiedBorrowing;
import com.opencbs.borrowings.repositories.custom.BorrowingRepositoryCustom;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class BorrowingRepositoryImpl extends BaseRepository<Borrowing> implements BorrowingRepositoryCustom {
    public BorrowingRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Borrowing.class);
    }

    @Override
    public List<Borrowing> getActiveBorrowings(LocalDateTime dateTime) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
        Root<Borrowing> root = cq.from(Borrowing.class);

        Predicate closedPred = this.getClosedPredicate(cb, cq, root, dateTime);
        Predicate activatedPred = this.getActivatedPredicate(cb, cq, root, dateTime);

        cq.select(root).where(cb.and(closedPred, activatedPred)).orderBy(cb.asc(root.get("id")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public List<Borrowing> getAllActiveNotInPivotCurrency(LocalDateTime dateTime, Long currencyId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
        Root<Borrowing> root = cq.from(Borrowing.class);
        Join<Object, Object> borrowingProduct = root.join("borrowingProduct", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> currency = borrowingProduct.join("currency", jakarta.persistence.criteria.JoinType.LEFT);

        Predicate closedPred = this.getClosedPredicate(cb, cq, root, dateTime);
        Predicate activatedPred = this.getActivatedPredicate(cb, cq, root, dateTime);
        Predicate notPivot = cb.notEqual(currency.get("id"), currencyId);

        cq.select(root).where(cb.and(closedPred, activatedPred, notPivot));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public Page<SimplifiedBorrowing> getAll(String searchString, Pageable pageable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Borrowing> root = cq.from(Borrowing.class);

        Join<Object, Object> createdBy = root.join("createdBy", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> profile = root.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> borrowingProduct = root.join("borrowingProduct", jakarta.persistence.criteria.JoinType.LEFT);

        Predicate predicate = null;
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.trim().toLowerCase() + "%";
            predicate = cb.or(
                    cb.like(cb.lower(root.get("code")), pattern),
                    cb.like(cb.lower(profile.get("name")), pattern)
            );
        }

        cq.multiselect(
                root.get("id").alias("id"),
                root.get("status").alias("status"),
                root.get("code").alias("code"),
                root.get("amount").alias("amount"),
                createdBy.get("firstName").alias("firstName"),
                createdBy.get("lastName").alias("lastName"),
                profile.get("name").alias("profileName"),
                root.get("interestRate").alias("interestRate"),
                root.get("createdAt").alias("createdAt"),
                root.get("scheduleType").alias("scheduleType"),
                borrowingProduct.get("name").alias("loanProductName")
        );

        if (predicate != null) cq.where(predicate);
        cq.distinct(true);
        cq.orderBy(cb.desc(root.get("createdAt")));

        // count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Borrowing> countRoot = countQuery.from(Borrowing.class);
        Join<Object, Object> countProfile = countRoot.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        if (predicate != null) {
            String pattern = "%" + searchString.trim().toLowerCase() + "%";
            Predicate countPred = cb.or(
                    cb.like(cb.lower(countRoot.get("code")), pattern),
                    cb.like(cb.lower(countProfile.get("name")), pattern)
            );
            countQuery.select(cb.countDistinct(countRoot)).where(countPred);
        } else {
            countQuery.select(cb.countDistinct(countRoot));
        }

        Long total = getEntityManager().createQuery(countQuery).getSingleResult();

        TypedQuery<Tuple> typedQuery = getEntityManager().createQuery(cq);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Tuple> tuples = typedQuery.getResultList();

        List<SimplifiedBorrowing> results = tuples.stream().map(t -> {
            SimplifiedBorrowing dto = new SimplifiedBorrowing();
            Object idObj = t.get("id");
            if (idObj instanceof Number) dto.setId(((Number) idObj).longValue());
            dto.setStatus((com.opencbs.borrowings.domain.enums.BorrowingStatus) t.get("status"));
            dto.setCode((String) t.get("code"));
            dto.setAmount((java.math.BigDecimal) t.get("amount"));
            dto.setFirstName((String) t.get("firstName"));
            dto.setLastName((String) t.get("lastName"));
            dto.setProfileName((String) t.get("profileName"));
            dto.setInterestRate((java.math.BigDecimal) t.get("interestRate"));
            dto.setCreatedAt((java.time.LocalDateTime) t.get("createdAt"));
            dto.setScheduleType((String) t.get("scheduleType"));
            dto.setLoanProductName((String) t.get("loanProductName"));
            return dto;
        }).toList();

        return new PageImpl<>(results, pageable, total);
    }

    

        private Predicate getClosedPredicate(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Borrowing> root, LocalDateTime dateTime) {
        Subquery<Long> sq = query.subquery(Long.class);
        Root<BorrowingEvent> closed = sq.from(BorrowingEvent.class);
        Predicate p = cb.and(
            cb.lessThanOrEqualTo(closed.get("effectiveAt"), dateTime),
            closed.get("eventType").in(EventType.CLOSED, EventType.WRITE_OFF_OLB),
            cb.notEqual(closed.get("deleted"), true)
        );
        sq.select(closed.get("borrowingId")).where(p);
        return cb.not(root.get("id").in(sq));
        }

        private Predicate getActivatedPredicate(CriteriaBuilder cb, CriteriaQuery<?> query, Root<Borrowing> root, LocalDateTime dateTime) {
        Subquery<Long> sq = query.subquery(Long.class);
        Root<BorrowingEvent> activated = sq.from(BorrowingEvent.class);
        Predicate p = cb.and(
            cb.lessThanOrEqualTo(activated.get("effectiveAt"), dateTime),
            activated.get("eventType").in(EventType.DISBURSEMENT),
            cb.notEqual(activated.get("deleted"), true)
        );
        sq.select(activated.get("borrowingId")).where(p);
        return root.get("id").in(sq);
        }

    // legacy projection helper removed; tuple-based queries are used instead
}
