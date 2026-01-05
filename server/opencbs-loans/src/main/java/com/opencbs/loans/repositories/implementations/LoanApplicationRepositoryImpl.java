package com.opencbs.loans.repositories.implementations;

import com.opencbs.core.domain.enums.EntityStatus;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.loans.domain.LoanApplication;
import com.opencbs.loans.domain.SimplifiedLoanApplication;
import com.opencbs.loans.domain.customfields.CollateralCustomFieldValue;
import com.opencbs.loans.domain.customfields.LoanApplicationCustomFieldValue;
import com.opencbs.loans.repositories.customs.LoanApplicationRepositoryCustom;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class LoanApplicationRepositoryImpl extends BaseRepository<LoanApplication> implements LoanApplicationRepositoryCustom {

    //@Autowired
    public LoanApplicationRepositoryImpl(EntityManager entityManager) {
        super(entityManager, LoanApplication.class);
    }

    @Override
    public Page<LoanApplication> findByProfile(Pageable pageable, Profile profile) {
        String sql = "select la from LoanApplication la " +
                "where la.profile.id = :profileId or la.id in " +
                "(select gla.loanApplication.id from GroupLoanApplication gla where gla.member.id = :profileId)";

        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("profileId", profile.getId());

        List<LoanApplication> loanApplications = query.getResultList();
        return new PageImpl<>(loanApplications, pageable, loanApplications.size());
    }

    @Override
    public Page<SimplifiedLoanApplication> findAllSimplifiedLoanApplication(String searchString, Pageable pageable, String order, Boolean isAsc) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<LoanApplication> root = cq.from(LoanApplication.class);
        Join<Object, Object> u = root.join("createdBy", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> lp = root.join("loanProduct", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> pr = root.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> br = root.join("branch", jakarta.persistence.criteria.JoinType.LEFT);

        Predicate predicate = null;
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.trim().toLowerCase() + "%";

            // subquery for LoanApplicationCustomFieldValue.owner.id
            Subquery<Long> lacfvSub = cq.subquery(Long.class);
            Root<LoanApplicationCustomFieldValue> lacfv = lacfvSub.from(LoanApplicationCustomFieldValue.class);
            lacfvSub.select(lacfv.get("owner").get("id")).where(cb.and(cb.like(cb.lower(lacfv.get("value")), pattern), cb.equal(lacfv.get("status"), EntityStatus.LIVE)));

            // subquery for CollateralCustomFieldValue -> collateral -> loanApplication.id
            Subquery<Long> ccfvSub = cq.subquery(Long.class);
            Root<CollateralCustomFieldValue> ccfv = ccfvSub.from(CollateralCustomFieldValue.class);
            Join<Object, Object> c = ccfv.join("collateral", jakarta.persistence.criteria.JoinType.LEFT);
            Join<Object, Object> toc = c.join("typeOfCollateral", jakarta.persistence.criteria.JoinType.LEFT);
            Predicate collPred = cb.disjunction();
            collPred = cb.or(collPred, cb.like(cb.lower(ccfv.get("value")), pattern));
            collPred = cb.or(collPred, cb.like(cb.lower(c.get("name")), pattern));
            collPred = cb.or(collPred, cb.like(cb.lower(toc.get("caption")), pattern));
            ccfvSub.select(c.get("loanApplication").get("id")).where(collPred);

            predicate = cb.and(cb.or(cb.like(cb.lower(pr.get("name")), pattern), cb.like(cb.lower(root.get("code")), pattern), cb.like(cb.lower(br.get("name")), pattern), root.get("id").in(lacfvSub), root.get("id").in(ccfvSub)));
        }

        // count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<LoanApplication> countRoot = countQuery.from(LoanApplication.class);
        if (predicate != null) {
            countQuery.select(cb.countDistinct(countRoot)).where(predicate);
        } else {
            countQuery.select(cb.countDistinct(countRoot));
        }
        Long total = getEntityManager().createQuery(countQuery).getSingleResult();

        cq.multiselect(
                root.get("id").alias("id"),
                root.get("status").alias("status"),
                pr.get("name").alias("profileName"),
                pr.get("type").alias("profileType"),
                root.get("amount").alias("amount"),
                lp.get("name").alias("loanProductName"),
                root.get("interestRate").alias("interestRate"),
                root.get("createdAt").alias("createdAt"),
                root.get("code").alias("code"),
                br.alias("branch")
        );

        if (predicate != null) cq.where(predicate);
        if (order != null) {
            if (isAsc != null && isAsc) cq.orderBy(cb.asc(root.get(order)));
            else cq.orderBy(cb.desc(root.get(order)));
        }

        TypedQuery<Tuple> typedQuery = getEntityManager().createQuery(cq);
        typedQuery.setMaxResults(pageable.getPageSize());
        typedQuery.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());

        List<Tuple> tuples = typedQuery.getResultList();
        List<SimplifiedLoanApplication> results = tuples.stream().map(t -> {
            SimplifiedLoanApplication dto = new SimplifiedLoanApplication();
            Object idObj = t.get("id");
            if (idObj instanceof Number) dto.setId(((Number) idObj).longValue());
            dto.setStatus((com.opencbs.loans.domain.enums.LoanApplicationStatus) t.get("status"));
            dto.setProfileName((String) t.get("profileName"));
            dto.setProfileType((String) t.get("profileType"));
            dto.setAmount((java.math.BigDecimal) t.get("amount"));
            dto.setLoanProductName((String) t.get("loanProductName"));
            dto.setInterestRate((java.math.BigDecimal) t.get("interestRate"));
            dto.setCreatedAt((java.time.LocalDateTime) t.get("createdAt"));
            dto.setCode((String) t.get("code"));
            dto.setBranch((com.opencbs.core.domain.Branch) t.get("branch"));
            return dto;
        }).toList();

        return new PageImpl<>(results, pageable, total);
    }

    // legacy Projection helper removed — using JPA CriteriaBuilder + Tuple instead
}
