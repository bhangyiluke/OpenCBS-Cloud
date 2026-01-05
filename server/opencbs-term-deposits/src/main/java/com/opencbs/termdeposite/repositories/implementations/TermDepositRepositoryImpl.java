package com.opencbs.termdeposite.repositories.implementations;

import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.termdeposite.domain.TermDeposit;
import com.opencbs.termdeposite.dto.TermDepositSimplified;
import com.opencbs.termdeposite.repositories.TermDepositRepositoryCustom;
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

public class TermDepositRepositoryImpl extends BaseRepository<TermDeposit> implements TermDepositRepositoryCustom {

    public TermDepositRepositoryImpl(EntityManager entityManager) {
        super(entityManager, TermDeposit.class);
    }

    private CriteriaQuery<TermDeposit> getCriteria() {
        // kept for compatibility; prefer using CriteriaBuilder in methods
        return null;
    }

    @Override
    public Page<TermDepositSimplified> getAllWithSearch(String searchString, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<TermDeposit> root = cq.from(TermDeposit.class);
        Join<Object, Object> profile = root.join("profile", JoinType.LEFT);
        Join<Object, Object> serviceOfficer = root.join("serviceOfficer", JoinType.LEFT);
        Join<Object, Object> product = root.join("termDepositProduct", JoinType.LEFT);

        List<Predicate> preds = new ArrayList<>();
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString + "%";
            preds.add(cb.or(
                    cb.like(cb.lower(root.get("code")), pattern.toLowerCase()),
                    cb.like(cb.lower(profile.get("name")), pattern.toLowerCase())
            ));
        }

        // Count
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<TermDeposit> countRoot = countCq.from(TermDeposit.class);
        List<Predicate> countPreds = new ArrayList<>();
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString + "%";
            countPreds.add(cb.or(
                    cb.like(cb.lower(countRoot.get("code")), pattern.toLowerCase()),
                    cb.like(cb.lower(countRoot.join("profile", JoinType.LEFT).get("name")), pattern.toLowerCase())
            ));
        }
        countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        cq.multiselect(
                root.get("id").alias("id"),
                root.get("code").alias("code"),
                root.get("status").alias("status"),
                profile.get("name").alias("profileName"),
                profile.get("id").alias("profileId"),
                serviceOfficer.get("firstName").alias("serviceOfficerFirstName"),
                serviceOfficer.get("lastName").alias("serviceOfficerLastName"),
                serviceOfficer.get("id").alias("serviceOfficerId"),
                root.get("openDate").alias("openDate"),
                product.get("name").alias("productName"),
                product.get("id").alias("productId"),
                root.get("createdAt").alias("createdAt")
        ).where(preds.toArray(new Predicate[0]));

        TypedQuery<Tuple> q = getEntityManager().createQuery(cq.orderBy(cb.desc(root.get("createdAt"))));
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        q.setMaxResults(pageable.getPageSize());

        List<Tuple> tuples = q.getResultList();
        List<TermDepositSimplified> result = new ArrayList<>();
        for (Tuple t : tuples) {
            TermDepositSimplified dto = new TermDepositSimplified();
            dto.setId(t.get("id", Long.class));
            dto.setCode(t.get("code", String.class));
            dto.setStatus(t.get("status", com.opencbs.termdeposite.domain.enums.TermDepositStatus.class));
            dto.setProfileName(t.get("profileName", String.class));
            dto.setProfileId(t.get("profileId", Long.class));
            dto.setServiceOfficerFirstName(t.get("serviceOfficerFirstName", String.class));
            dto.setServiceOfficerLastName(t.get("serviceOfficerLastName", String.class));
            dto.setServiceOfficerId(t.get("serviceOfficerId", Long.class));
            dto.setOpenDate(t.get("openDate", java.time.LocalDateTime.class));
            dto.setProductName(t.get("productName", String.class));
            dto.setProductId(t.get("productId", Long.class));
            dto.setCreatedAt(t.get("createdAt", java.time.LocalDateTime.class));
            result.add(dto);
        }

        return new PageImpl<>(result, pageable, total);
    }

    // legacy ProjectionList helper removed — queries use CriteriaBuilder + Tuple
}
