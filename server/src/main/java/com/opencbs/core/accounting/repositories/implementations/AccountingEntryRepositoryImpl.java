package com.opencbs.core.accounting.repositories.implementations;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.accounting.domain.AccountingEntry;
import com.opencbs.core.accounting.dto.SortedAccountingEntryDto;
import com.opencbs.core.accounting.repositories.customs.AccountingEntryRepositoryCustom;
import com.opencbs.core.repositories.implementations.BaseRepository;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SuppressWarnings("unused")
public class AccountingEntryRepositoryImpl extends BaseRepository<AccountingEntry> implements AccountingEntryRepositoryCustom {

    //@Autowired
    public AccountingEntryRepositoryImpl(EntityManager entityManager) {
        super(entityManager, AccountingEntry.class);
    }

    @Override
    public List<AccountingEntry> getAccountingEntriesByAccount(Account account, LocalDateTime from, LocalDateTime to) {
        String sql = "select a from AccountingEntry a " +
                "where a.effectiveAt between :from and :to " +
                "and (a.debitAccount.id = :accountId or a.creditAccount.id = :accountId)" +
                "and (a.deleted = false)";

        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("from", from);
        query.setParameter("to", to);
        query.setParameter("accountId", account.getId());
        return query.getResultList();
    }

    @Override
    public Boolean hasTransactions(Long accountId) {
        String hql = "select case when count(*) > 0 then true else false end\n " +
                "from AccountingEntry as ae\n " +
                "where ae.debitAccount.id = :accountId or ae.creditAccount.id = :accountId\n " +
                "and now() > ae.createdAt";
        Query query = this.getEntityManager().createQuery(hql);
        query.setParameter("accountId", accountId);
        return (Boolean) query.getSingleResult();
    }

    @Override
    public List<AccountingEntry> getAccountingEntries(LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<AccountingEntry> cq = cb.createQuery(AccountingEntry.class);
        Root<AccountingEntry> root = cq.from(AccountingEntry.class);

        Predicate p = cb.and(
                cb.greaterThanOrEqualTo(root.get("effectiveAt"), startDate),
                cb.lessThan(root.get("effectiveAt"), endDate),
                cb.equal(root.get("deleted"), false)
        );

        cq.select(root).where(p).distinct(true);
        TypedQuery<AccountingEntry> query = getEntityManager().createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Page<AccountingEntry> getAll(SortedAccountingEntryDto sortedAccountingEntryDto, Pageable pageable, List<Long> accountIds) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<AccountingEntry> cq = cb.createQuery(AccountingEntry.class);
        Root<AccountingEntry> root = cq.from(AccountingEntry.class);

        Join<AccountingEntry, ?> debAccount = root.join("debitAccount", JoinType.LEFT);
        Join<AccountingEntry, ?> creAccount = root.join("creditAccount", JoinType.LEFT);
        Join<AccountingEntry, ?> createdBy = root.join("createdBy", JoinType.LEFT);
        root.join("branch", JoinType.LEFT);

        // predicates
        Predicate deletedFalse = cb.equal(root.get("deleted"), false);
        java.util.List<Predicate> predicates = new java.util.ArrayList<>();
        predicates.add(deletedFalse);

        if (accountIds != null && !accountIds.isEmpty()) {
            predicates.add(cb.or(debAccount.get("id").in(accountIds), creAccount.get("id").in(accountIds)));
        }

        if (sortedAccountingEntryDto.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveAt"), LocalDateTime.of(sortedAccountingEntryDto.getFromDate(), LocalTime.MIN)));
        }

        if (sortedAccountingEntryDto.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("effectiveAt"), LocalDateTime.of(sortedAccountingEntryDto.getToDate(), LocalTime.MAX)));
        }

        if (!sortedAccountingEntryDto.getShowSystem()) {
            predicates.add(cb.notEqual(createdBy.get("isSystemUser"), true));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0])).distinct(true);
        cq.orderBy(cb.desc(root.get("effectiveAt")), cb.asc(root.get("id")));

        // total count
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<AccountingEntry> countRoot = countCq.from(AccountingEntry.class);
        // apply similar joins and predicates on count query
        Join<AccountingEntry, ?> countDeb = countRoot.join("debitAccount", JoinType.LEFT);
        Join<AccountingEntry, ?> countCre = countRoot.join("creditAccount", JoinType.LEFT);
        Join<AccountingEntry, ?> countCreatedBy = countRoot.join("createdBy", JoinType.LEFT);
        java.util.List<Predicate> countPreds = new java.util.ArrayList<>();
        countPreds.add(cb.equal(countRoot.get("deleted"), false));
        if (accountIds != null && !accountIds.isEmpty()) {
            countPreds.add(cb.or(countDeb.get("id").in(accountIds), countCre.get("id").in(accountIds)));
        }
        if (sortedAccountingEntryDto.getFromDate() != null) {
            countPreds.add(cb.greaterThanOrEqualTo(countRoot.get("effectiveAt"), LocalDateTime.of(sortedAccountingEntryDto.getFromDate(), LocalTime.MIN)));
        }
        if (sortedAccountingEntryDto.getToDate() != null) {
            countPreds.add(cb.lessThanOrEqualTo(countRoot.get("effectiveAt"), LocalDateTime.of(sortedAccountingEntryDto.getToDate(), LocalTime.MAX)));
        }
        if (!sortedAccountingEntryDto.getShowSystem()) {
            countPreds.add(cb.notEqual(countCreatedBy.get("isSystemUser"), true));
        }
        countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        TypedQuery<AccountingEntry> typed = getEntityManager().createQuery(cq);
        typed.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typed.setMaxResults(pageable.getPageSize());

        List<AccountingEntry> results = typed.getResultList();
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<AccountingEntry> getAll(SortedAccountingEntryDto sortedAccountingEntryDto, Pageable pageable) {
        return this.getAll(sortedAccountingEntryDto, pageable, null);
    }

    // legacy helper methods (Hibernate Criteria) removed in favor of JPA Criteria
}
