package com.opencbs.savings.repositories.implementations;

import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.savings.domain.Saving;
import com.opencbs.savings.domain.SavingSimplified;
import com.opencbs.savings.domain.enums.SavingAccountRuleType;
import com.opencbs.savings.dto.SavingWithAccountDto;
import com.opencbs.savings.repositories.customs.SavingRepositoryCustom;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import java.util.List;

@SuppressWarnings("unused")
public class SavingRepositoryImpl extends BaseRepository<Saving> implements SavingRepositoryCustom {

    public SavingRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Saving.class);
    }

    @Override
    public Page<SavingSimplified> getAll(String searchString, Pageable pageable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Saving> root = cq.from(Saving.class);

        Join<Object, Object> profile = root.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> savingOfficer = root.join("savingOfficer", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> product = root.join("product", jakarta.persistence.criteria.JoinType.LEFT);

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
                root.get("code").alias("code"),
                root.get("status").alias("status"),
                profile.get("name").alias("profileName"),
                profile.get("id").alias("profileId"),
                savingOfficer.get("firstName").alias("savingOfficerFirstName"),
                savingOfficer.get("lastName").alias("savingOfficerLastName"),
                savingOfficer.get("id").alias("savingOfficerId"),
                root.get("openDate").alias("openDate"),
                product.get("name").alias("productName"),
                product.get("id").alias("productId")
        );
        if (predicate != null) cq.where(predicate);
        cq.orderBy(cb.desc(root.get("createdAt")));
        cq.distinct(true);

        // count
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Saving> countRoot = countQuery.from(Saving.class);
        Join<Object, Object> countProfile = countRoot.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        if (predicate != null) {
            // rebuild predicate for count using countRoot & countProfile
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

        List<SavingSimplified> results = tuples.stream().map(t -> {
            SavingSimplified dto = new SavingSimplified();
            Object idObj = t.get("id");
            if (idObj instanceof Number) dto.setId(((Number) idObj).longValue());
            dto.setCode((String) t.get("code"));
            dto.setStatus((com.opencbs.savings.domain.enums.SavingStatus) t.get("status"));
            dto.setProfileName((String) t.get("profileName"));
            Object profileIdObj = t.get("profileId");
            if (profileIdObj instanceof Number) dto.setProfileId(((Number) profileIdObj).longValue());
            dto.setSavingOfficerFirstName((String) t.get("savingOfficerFirstName"));
            dto.setSavingOfficerLastName((String) t.get("savingOfficerLastName"));
            Object savingOfficerIdObj = t.get("savingOfficerId");
            if (savingOfficerIdObj instanceof Number) dto.setSavingOfficerId(((Number) savingOfficerIdObj).longValue());
            dto.setOpenDate((java.time.LocalDateTime) t.get("openDate"));
            dto.setProductName((String) t.get("productName"));
            Object productIdObj = t.get("productId");
            if (productIdObj instanceof Number) dto.setProductId(((Number) productIdObj).longValue());
            return dto;
        }).toList();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<SavingWithAccountDto> getAllSimplifiedSavingAccount(String searchString, Pageable pageable) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Saving> root = cq.from(Saving.class);

        Join<Object, Object> p = root.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> sp = root.join("product", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> currentAccount = p.join("currentAccounts", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> savingAccount = root.join("accounts", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> c2 = currentAccount.join("currency", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> account = savingAccount.join("account", jakarta.persistence.criteria.JoinType.LEFT);

        Predicate wherePredicate = null;
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.trim().toLowerCase() + "%";
            wherePredicate = cb.or(
                    cb.like(cb.lower(account.get("number")), pattern),
                    cb.like(cb.lower(p.get("name")), pattern)
            );
        }

        Predicate criterion = cb.and(
                cb.equal(savingAccount.get("type"), SavingAccountRuleType.SAVING),
                cb.equal(currentAccount.get("currency"), sp.get("currency"))
        );

        if (wherePredicate != null) cq.where(cb.and(wherePredicate, criterion));
        else cq.where(criterion);

        cq.multiselect(
                currentAccount.get("id").alias("id"),
                p.get("name").alias("name"),
                account.get("number").alias("number"),
                account.get("id").alias("accountId"),
                p.get("id").alias("profileId"),
                root.get("id").alias("savingId"),
                c2.get("name").alias("currency")
        );
        cq.distinct(true);

        // count distinct currentAccount id
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Saving> countRoot = countQuery.from(Saving.class);
        Join<Object, Object> countP = countRoot.join("profile", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> countSp = countRoot.join("product", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> countCurrentAccount = countP.join("currentAccounts", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> countSavingAccount = countRoot.join("accounts", jakarta.persistence.criteria.JoinType.LEFT);
        Join<Object, Object> countAccount = countSavingAccount.join("account", jakarta.persistence.criteria.JoinType.LEFT);

        Predicate countCriterion = cb.and(
                cb.equal(countSavingAccount.get("type"), SavingAccountRuleType.SAVING),
                cb.equal(countCurrentAccount.get("currency"), countSp.get("currency"))
        );
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.trim().toLowerCase() + "%";
            Predicate countWhere = cb.or(
                    cb.like(cb.lower(countAccount.get("number")), pattern),
                    cb.like(cb.lower(countP.get("name")), pattern)
            );
            countQuery.select(cb.countDistinct(countCurrentAccount.get("id"))).where(cb.and(countWhere, countCriterion));
        } else {
            countQuery.select(cb.countDistinct(countCurrentAccount.get("id"))).where(countCriterion);
        }

        Long total = getEntityManager().createQuery(countQuery).getSingleResult();

        TypedQuery<Tuple> typedQuery = getEntityManager().createQuery(cq);
        typedQuery.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Tuple> tuples = typedQuery.getResultList();
        List<SavingWithAccountDto> results = tuples.stream().map(t -> {
            SavingWithAccountDto dto = new SavingWithAccountDto();
            Object idObj = t.get("id");
            if (idObj instanceof Number) dto.setId(((Number) idObj).longValue());
            dto.setName((String) t.get("name"));
            dto.setNumber((String) t.get("number"));
            Object accountIdObj = t.get("accountId");
            if (accountIdObj instanceof Number) dto.setAccountId(((Number) accountIdObj).longValue());
            Object profileIdObj = t.get("profileId");
            if (profileIdObj instanceof Number) dto.setProfileId(((Number) profileIdObj).longValue());
            Object savingIdObj = t.get("savingId");
            if (savingIdObj instanceof Number) dto.setSavingId(((Number) savingIdObj).longValue());
            dto.setCurrency((String) t.get("currency"));
            return dto;
        }).toList();

        return new PageImpl<>(results, pageable, total);
    }
}
