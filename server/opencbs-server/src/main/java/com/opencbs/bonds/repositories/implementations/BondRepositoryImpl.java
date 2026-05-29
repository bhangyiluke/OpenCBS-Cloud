package com.opencbs.bonds.repositories.implementations;

import com.opencbs.bonds.domain.Bond;
import com.opencbs.bonds.domain.SimplifiedBond;
import com.opencbs.bonds.repositories.custom.BondRepositoryCustom;
import com.opencbs.core.repositories.implementations.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BondRepositoryImpl extends BaseRepository<Bond> implements BondRepositoryCustom {

    public BondRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Bond.class);
    }

    @Override
    public Page<SimplifiedBond> findAllSimplifiedBonds(String searchQuery, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Bond> root = cq.from(Bond.class);
        Join<Object, Object> createdBy = root.join("createdBy", JoinType.LEFT);
        Join<Object, Object> profile = root.join("profile", JoinType.LEFT);

        List<Predicate> preds = new ArrayList<>();
        if (!StringUtils.isEmpty(searchQuery)) {
            String pattern = "%" + searchQuery + "%";
            preds.add(cb.or(
                    cb.like(cb.lower(profile.get("name")), pattern.toLowerCase()),
                    cb.like(cb.lower(root.get("isin")), pattern.toLowerCase())
            ));
        }

        cq.multiselect(
                root.get("id").alias("id"),
                root.get("isin").alias("isin"),
                root.get("status").alias("status"),
                root.get("interestRate").alias("interestRate"),
                root.get("number").alias("number"),
                profile.get("id").alias("profileId"),
                profile.get("name").alias("profileName"),
                root.get("amount").alias("amount"),
                root.get("createdAt").alias("createdAt"),
                createdBy.get("id").alias("createdById"),
                createdBy.get("firstName").alias("createdByFirstName"),
                createdBy.get("lastName").alias("createdByLastName")
        ).where(preds.toArray(new Predicate[0]));

        // count
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Bond> countRoot = countCq.from(Bond.class);
        List<Predicate> countPreds = new ArrayList<>();
        if (!StringUtils.isEmpty(searchQuery)) {
            String pattern = "%" + searchQuery + "%";
            countPreds.add(cb.or(
                    cb.like(cb.lower(countRoot.join("profile", JoinType.LEFT).get("name")), pattern.toLowerCase()),
                    cb.like(cb.lower(countRoot.get("isin")), pattern.toLowerCase())
            ));
        }
        countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        TypedQuery<Tuple> q = getEntityManager().createQuery(cq);
        q.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        q.setMaxResults(pageable.getPageSize());
        List<Tuple> tuples = q.getResultList();

        List<SimplifiedBond> results = new ArrayList<>();
        for (Tuple t : tuples) {
            SimplifiedBond sb = new SimplifiedBond();
            sb.setId(t.get("id", Long.class));
            sb.setIsin(t.get("isin", String.class));
            sb.setStatus(t.get("status", com.opencbs.bonds.domain.enums.BondStatus.class));
            sb.setInterestRate(t.get("interestRate", java.math.BigDecimal.class));
            sb.setNumber(t.get("number", Integer.class));
            sb.setProfileId(t.get("profileId", Long.class));
            sb.setProfileName(t.get("profileName", String.class));
            sb.setAmount(t.get("amount", java.math.BigDecimal.class));
            sb.setCreatedAt(t.get("createdAt", java.time.LocalDateTime.class));
            sb.setCreatedById(t.get("createdById", Long.class));
            sb.setCreatedByFirstName(t.get("createdByFirstName", String.class));
            sb.setCreatedByLastName(t.get("createdByLastName", String.class));
            results.add(sb);
        }

        return new PageImpl<>(results, pageable, total);

    }

    private CriteriaQuery<Bond> getCriteria(){
        // kept for compatibility if needed elsewhere; prefer using CriteriaBuilder in methods
        return null;
    }
}