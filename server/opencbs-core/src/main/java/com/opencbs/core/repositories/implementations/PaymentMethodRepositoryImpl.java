package com.opencbs.core.repositories.implementations;

import com.opencbs.core.domain.trees.PaymentMethod;
import com.opencbs.core.repositories.customs.PaymentMethodRepositoryCustom;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

@SuppressWarnings("unused")
public class PaymentMethodRepositoryImpl extends TreeEntityRepositoryImpl<PaymentMethod> implements PaymentMethodRepositoryCustom {

    //@Autowired
    protected PaymentMethodRepositoryImpl(EntityManager entityManager) {
        super(entityManager, PaymentMethod.class);
    }

    @Override
    public Page<PaymentMethod> search(String query, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<PaymentMethod> cq = cb.createQuery(PaymentMethod.class);
        Root<PaymentMethod> root = cq.from(PaymentMethod.class);

        if (query != null && !query.isEmpty()) {
            String pattern = "%" + query.toLowerCase() + "%";
            Predicate p = cb.like(cb.lower(root.get("name")), pattern);
            cq.where(p);
        }

        TypedQuery<PaymentMethod> typed = getEntityManager().createQuery(cq.select(root));
        typed.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typed.setMaxResults(pageable.getPageSize());

        List<PaymentMethod> paymentMethods = typed.getResultList();

        // count
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<PaymentMethod> countRoot = countCq.from(PaymentMethod.class);
        if (query != null && !query.isEmpty()) {
            String pattern = "%" + query.toLowerCase() + "%";
            countCq.where(cb.like(cb.lower(countRoot.get("name")), pattern));
        }
        countCq.select(cb.count(countRoot));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        return new PageImpl<>(paymentMethods, pageable, total);
    }
}
