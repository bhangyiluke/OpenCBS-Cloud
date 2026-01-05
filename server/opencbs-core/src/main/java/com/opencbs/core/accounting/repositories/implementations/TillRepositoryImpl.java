package com.opencbs.core.accounting.repositories.implementations;

import com.opencbs.core.accounting.repositories.customs.TillRepositoryCustom;
import com.opencbs.core.domain.Currency;
import com.opencbs.core.domain.User;
import com.opencbs.core.domain.till.Operation;
import com.opencbs.core.domain.till.Till;
import com.opencbs.core.repositories.implementations.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class TillRepositoryImpl extends BaseRepository<Till> implements TillRepositoryCustom {
    protected TillRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Till.class);
    }

    @Override
    public Page<Operation> getOperationsByCurrency(Pageable pageable, Till till, Currency currency, LocalDateTime fromDate, LocalDateTime toDate) {
        return this.getOperations(pageable, till.getId(), currency.getId(), fromDate, toDate);
    }

    @Override
    public Page<Operation> getOperations(Pageable pageable, Till till, LocalDateTime fromDate, LocalDateTime toDate) {
        return this.getOperations(pageable, till.getId(), 0, fromDate, toDate);
    }

    @Override
    public Page<Till> search(Pageable pageable, String searchString) {
        String queryString = "from tills where \"name\" ilike :searchString";
        Query totalQuery = this.getEntityManager().createNativeQuery("select count(*) " + queryString);
        totalQuery.setParameter("searchString", String.format("%%%s%%", searchString));
        BigInteger total = (BigInteger) totalQuery.getSingleResult();

        Query query = this.getEntityManager().createNativeQuery("select * " + queryString, Till.class);
        query.setParameter("searchString", String.format("%%%s%%", searchString));
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, total.longValue());
    }

    private Page<Operation> getOperations(Pageable pageable, long tillId, long currencyId, LocalDateTime fromDate, LocalDateTime toDate) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Operation> cq = cb.createQuery(Operation.class);
        Root<Operation> root = cq.from(Operation.class);

        List<Predicate> preds = new ArrayList<>();
        preds.add(cb.equal(root.get("tillId"), tillId));
        preds.add(cb.between(root.get("effectiveAt"), fromDate, toDate));
        if (currencyId != 0) {
            preds.add(cb.equal(root.get("currency").get("id"), currencyId));
        }

        cq.select(root).where(preds.toArray(new Predicate[0]));
        cq.orderBy(cb.desc(root.get("effectiveAt")));

        TypedQuery<Operation> q = getEntityManager().createQuery(cq);
        q.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        q.setMaxResults(pageable.getPageSize());
        List<Operation> operations = q.getResultList();

        // Count
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Operation> countRoot = countCq.from(Operation.class);
        List<Predicate> countPreds = new ArrayList<>();
        countPreds.add(cb.equal(countRoot.get("tillId"), tillId));
        countPreds.add(cb.between(countRoot.get("effectiveAt"), fromDate, toDate));
        if (currencyId != 0) {
            countPreds.add(cb.equal(countRoot.get("currency").get("id"), currencyId));
        }
        countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        return new PageImpl<>(operations, pageable, total);
    }

    

    @Override
    public Page<User> findAllTeller(Pageable pageable, String searchString) {
        String queryString = "from users u\n" +
                " left join roles_permissions r on r.role_id = u.role_id\n"+
                " where (lower (u.first_name) || lower (u.last_name) like lower (:searchString))\n"+
                " and r.permission_id in (4, 3)";
        Query totalQuery = this.getEntityManager().createNativeQuery("select count(*)" + queryString);
        totalQuery.setParameter("searchString", String.format("%%%s%%", searchString));
        BigInteger total = (BigInteger) totalQuery.getSingleResult();

        Query query = this.getEntityManager().createNativeQuery("select distinct u.*" + queryString, User.class);
        query.setParameter("searchString", String.format("%%%s%%", searchString));
        query.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(query.getResultList(), pageable, total.longValue());
    }
}
