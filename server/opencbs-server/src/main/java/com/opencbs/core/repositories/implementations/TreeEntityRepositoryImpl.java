package com.opencbs.core.repositories.implementations;

import com.opencbs.core.domain.trees.TreeEntity;
import com.opencbs.core.repositories.customs.TreeEntityRepositoryCustom;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;

@SuppressWarnings("unused")
public abstract class TreeEntityRepositoryImpl<Tte extends TreeEntity> extends BaseRepository implements TreeEntityRepositoryCustom {

    protected TreeEntityRepositoryImpl(EntityManager entityManager, Class<Tte> tClass) {
        super(entityManager, tClass);
    }

    @Override
    public Page<Tte> findBy(String query, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Tte> cq = cb.createQuery((Class<Tte>) this.clazz);
        Root<Tte> root = cq.from((Class<Tte>) this.clazz);

        Predicate valuePredicate = null;
        if (query != null && !query.isEmpty()) {
            valuePredicate = cb.like(cb.lower(root.get("name")), "%" + query.toLowerCase() + "%");
        }

        // leaves subquery: select p.parent.id where parent is not null
        Subquery<Long> sub = cq.subquery(Long.class);
        Root<Tte> p = sub.from((Class<Tte>) this.clazz);
        sub.select(p.get("parent").get("id")).where(cb.isNotNull(p.get("parent").get("id")));
        Predicate leavesNotIn = cb.not(root.get("id").in(sub));

        Predicate finalPredicate = leavesNotIn;
        if (valuePredicate != null) {
            finalPredicate = cb.and(valuePredicate, leavesNotIn);
        }

        cq.select(root).where(finalPredicate).distinct(true).orderBy(cb.asc(root.get("name")));

        TypedQuery<Tte> tq = getEntityManager().createQuery(cq);
        tq.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        tq.setMaxResults(pageable.getPageSize());
        java.util.List<Tte> results = tq.getResultList();

        // count
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<Tte> countRoot = countQ.from((Class<Tte>) this.clazz);
        Subquery<Long> countSub = countQ.subquery(Long.class);
        Root<Tte> cp = countSub.from((Class<Tte>) this.clazz);
        countSub.select(cp.get("parent").get("id")).where(cb.isNotNull(cp.get("parent").get("id")));
        Predicate countLeavesNotIn = cb.not(countRoot.get("id").in(countSub));

        if (valuePredicate != null) {
            countQ.select(cb.count(countRoot)).where(cb.and(cb.like(cb.lower(countRoot.get("name")), "%" + query.toLowerCase() + "%"), countLeavesNotIn));
        } else {
            countQ.select(cb.count(countRoot)).where(countLeavesNotIn);
        }

        Long total = getEntityManager().createQuery(countQ).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<Tte> findLeaves(Pageable pageable) {
        return findBy(null, pageable);
    }
}
