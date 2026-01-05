package com.opencbs.loans.repositories.implementations;

import com.opencbs.core.domain.User;
import com.opencbs.core.domain.enums.EntityStatus;
import com.opencbs.core.helpers.DateHelper;
import com.opencbs.core.helpers.FileProvider;
import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.loans.domain.Loan;
import com.opencbs.loans.domain.LoanAdditionalInfo;
import com.opencbs.loans.domain.SimplifiedLoan;
import com.opencbs.loans.domain.customfields.CollateralCustomFieldValue;
import com.opencbs.loans.domain.customfields.LoanApplicationCustomFieldValue;
import com.opencbs.loans.domain.enums.LoanStatus;
import com.opencbs.loans.repositories.customs.LoanRepositoryCustom;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Repository
public class LoanRepositoryImpl extends BaseRepository<Loan> implements LoanRepositoryCustom {

    protected LoanRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Loan.class);
    }

    public Page<SimplifiedLoan> findAllLoans(String searchString, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Loan> root = cq.from(Loan.class);

        Predicate predicate = null;
        if (!StringUtils.isEmpty(searchString)) {
            searchString = searchString.trim();
            BigDecimal amountSearchPattern = null;
            try{
                amountSearchPattern = new BigDecimal(searchString);
            }catch (NumberFormatException exc){
            }

            // subquery for LoanApplicationCustomFieldValue.owner.id
            Subquery<Long> lacfvSub = cq.subquery(Long.class);
            Root<LoanApplicationCustomFieldValue> lacfv = lacfvSub.from(LoanApplicationCustomFieldValue.class);
            lacfvSub.select(lacfv.get("owner").get("id")).where(cb.and(cb.like(cb.lower(lacfv.get("value")), "%" + searchString.toLowerCase() + "%"), cb.equal(lacfv.get("status"), EntityStatus.LIVE)));

            // subquery for CollateralCustomFieldValue -> collateral -> loanApplication.id
            Subquery<Long> ccfvSub = cq.subquery(Long.class);
            Root<CollateralCustomFieldValue> ccfv = ccfvSub.from(CollateralCustomFieldValue.class);
            Join<Object, Object> c = ccfv.join("collateral", jakarta.persistence.criteria.JoinType.LEFT);
            Join<Object, Object> toc = c.join("typeOfCollateral", jakarta.persistence.criteria.JoinType.LEFT);
            Predicate collPred = cb.disjunction();
            collPred = cb.or(collPred, cb.like(cb.lower(ccfv.get("value")), "%" + searchString.toLowerCase() + "%"));
            collPred = cb.or(collPred, cb.like(cb.lower(c.get("name")), "%" + searchString.toLowerCase() + "%"));
            collPred = cb.or(collPred, cb.like(cb.lower(toc.get("caption")), "%" + searchString.toLowerCase() + "%"));
            ccfvSub.select(c.get("loanApplication").get("id")).where(collPred);

            // guarantors subquery
            Subquery<Long> guarantorsSub = cq.subquery(Long.class);
            Root<Loan> loanSub = guarantorsSub.from(Loan.class);
            Join<Object, Object> la = loanSub.join("loanApplication", jakarta.persistence.criteria.JoinType.LEFT);
            Join<Object, Object> g = la.join("guarantors", jakarta.persistence.criteria.JoinType.LEFT);
            Join<Object, Object> p = g.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
            guarantorsSub.select(la.get("id")).where(cb.like(cb.lower(p.get("name")), "%" + searchString.toLowerCase() + "%"));

            // loan officer subquery
            Subquery<Long> loanOfficerSub = cq.subquery(Long.class);
            Root<User> loanOfficer = loanOfficerSub.from(User.class);
            Predicate loanOfficerPred = cb.or(cb.like(cb.lower(loanOfficer.get("firstName")), "%" + searchString.toLowerCase() + "%"), cb.like(cb.lower(loanOfficer.get("lastName")), "%" + searchString.toLowerCase() + "%"));
            loanOfficerSub.select(loanOfficer.get("id")).where(loanOfficerPred);

            predicate = cb.and(
                    cb.or(
                            cb.like(cb.lower(root.get("profileName")), "%" + searchString.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("type")), "%" + searchString.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("code")), "%" + searchString.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("branchName")), "%" + searchString.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("applicationCode")), "%" + searchString.toLowerCase() + "%"),
                            root.get("applicationId").in(lacfvSub),
                            root.get("applicationId").in(ccfvSub),
                            root.get("applicationId").in(guarantorsSub),
                            root.get("loanOfficerId").in(loanOfficerSub),
                            cb.like(cb.lower(root.get("creditLine")), "%" + searchString.toLowerCase() + "%")
                    ),
                    cb.notEqual(root.get("status"), LoanStatus.PENDING)
            );
        }

        // count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Loan> countRoot = countQuery.from(Loan.class);
        if (predicate != null) countQuery.select(cb.countDistinct(countRoot)).where(predicate);
        else countQuery.select(cb.countDistinct(countRoot));
        Long total = getEntityManager().createQuery(countQuery).getSingleResult();

        // select projection fields
        cq.multiselect(
                root.get("id").alias("id"),
                root.get("profileName").alias("profileName"),
                root.get("amount").alias("amount"),
                root.get("code").alias("code"),
                root.get("interestRate").alias("interestRate"),
                root.get("applicationId").alias("applicationId"),
                root.get("applicationCode").alias("applicationCode"),
                root.get("type").alias("type"),
                root.get("productName").alias("productName"),
                root.get("createdBy").alias("createdBy"),
                root.get("status").alias("status"),
                root.get("createdAt").alias("createdAt"),
                root.get("branchName").alias("branchName"),
                root.get("currency").alias("currency"),
                root.get("disbursementDate").alias("disbursementDate"),
                root.get("maturityDate").alias("maturityDate"),
                root.get("loanOfficerId").alias("loanOfficerId"),
                root.get("creditLine").alias("creditLine"),
                root.get("creditLineOutstandingAmount").alias("creditLineOutstandingAmount")
        );

        if (predicate != null) cq.where(predicate);
        cq.orderBy(cb.desc(root.get("createdAt")));

        TypedQuery<Tuple> typedQuery = getEntityManager().createQuery(cq);
        typedQuery.setMaxResults(pageable.getPageSize());
        typedQuery.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());

        List<Tuple> tuples = typedQuery.getResultList();
        List<SimplifiedLoan> results = tuples.stream().map(t -> {
            SimplifiedLoan dto = new SimplifiedLoan();
            Object idObj = t.get("id");
            if (idObj instanceof Number) dto.setId(((Number) idObj).longValue());
            dto.setProfileName((String) t.get("profileName"));
            dto.setAmount((java.math.BigDecimal) t.get("amount"));
            dto.setCode((String) t.get("code"));
            dto.setInterestRate((java.math.BigDecimal) t.get("interestRate"));
            dto.setApplicationId((Long) t.get("applicationId"));
            dto.setApplicationCode((String) t.get("applicationCode"));
            dto.setType((String) t.get("type"));
            dto.setProductName((String) t.get("productName"));
            dto.setCreatedBy((String) t.get("createdBy"));
            dto.setStatus((LoanStatus) t.get("status"));
            dto.setCreatedAt((java.time.LocalDateTime) t.get("createdAt"));
            dto.setBranchName((String) t.get("branchName"));
            dto.setCurrency((String) t.get("currency"));
            dto.setDisbursementDate((java.time.LocalDate) t.get("disbursementDate"));
            dto.setMaturityDate((java.time.LocalDate) t.get("maturityDate"));
            dto.setLoanOfficerId(t.get("loanOfficerId") == null ? null : ((Number) t.get("loanOfficerId")).longValue());
            dto.setCreditLine((String) t.get("creditLine"));
            dto.setCreditLineOutstandingAmount((java.math.BigDecimal) t.get("creditLineOutstandingAmount"));
            return dto;
        }).toList();

        return new PageImpl<>(results, pageable, total);
    }

    // legacy Projection helper removed — using JPA CriteriaBuilder + Tuple instead

    @Override
    public LoanAdditionalInfo getAdditionalInfo(Long loanId) throws Exception {
        String queryString = FileProvider.getLoanScript("LoanAdditionalInfoScript.sql");
        Query query = this.getEntityManager().createNativeQuery(queryString,"loanAdditionalInfoMapper");
        query.setParameter("loanId", loanId);
        query.setParameter("dateTime", DateHelper.getLocalDateTimeNow());
        try {
            LoanAdditionalInfo result = (LoanAdditionalInfo) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            throw new Exception("Additional information on the loan was not found.");
        }
     }
}
