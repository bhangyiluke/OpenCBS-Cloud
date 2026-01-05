package com.opencbs.core.accounting.repositories.implementations;

import com.opencbs.core.accounting.domain.Account;
import com.opencbs.core.accounting.domain.AccountExtendedTag;
import com.opencbs.core.accounting.repositories.customs.AccountRepositoryCustom;
import com.opencbs.core.domain.Branch;
import com.opencbs.core.domain.enums.AccountType;
import com.opencbs.core.dto.requests.AccountRequest;
import com.opencbs.core.repositories.implementations.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
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
public class AccountRepositoryImpl extends BaseRepository<Account> implements AccountRepositoryCustom {

    //@Autowired
    public AccountRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Account.class);
    }

    @Override
    public List<Account> findRootAccounts() {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(clazz);
        Root<Account> root = cq.from(clazz);
        cq.select(root).where(cb.isNull(root.get("parent")));
        return toTypedQuery(cq).getResultList();
    }

    @Override
    public List<Account> findLeavesByParent(Pageable pageable, Account account) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(clazz);
        Root<Account> root = cq.from(clazz);
        cq.select(root).where(cb.equal(root.get("parent"), account));
        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<Account> q = toTypedQuery(cq);
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        q.setMaxResults(pageable.getPageSize());
        return q.getResultList();
    }

    @Override
    public List<Account> findLeavesByParentAndBranch(Pageable pageable, Account account, Branch branch) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(clazz);
        Root<Account> root = cq.from(clazz);
        Predicate p = cb.and(cb.equal(root.get("parent"), account), cb.equal(root.get("branch"), branch));
        cq.select(root).where(p).orderBy(cb.asc(root.get("id")));

        TypedQuery<Account> q = toTypedQuery(cq);
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        q.setMaxResults(pageable.getPageSize());
        return q.getResultList();
    }

    @Override
    public Page<Account> search(Long currencyId, String searchString, List<AccountType> accountTypes, AccountRequest.TypeOfAccount typeOfAccount, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(clazz);
        Root<Account> root = cq.from(clazz);
        Join<Object, Object> currency = root.join("currency", JoinType.LEFT);
        Join<Object, Object> branch = root.join("branch", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("number")), pattern),
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(currency.get("name")), pattern),
                    cb.like(cb.lower(branch.get("name")), pattern)
            ));
        }
        if (currencyId != null) {
            predicates.add(cb.equal(currency.get("id"), currencyId));
        }
        if (accountTypes != null && !accountTypes.isEmpty()) {
            predicates.add(root.get("type").in(accountTypes));
        }
        if (typeOfAccount != null) {
            predicates.add(cb.equal(root.get("isDebit"), (AccountRequest.TypeOfAccount.DEBIT.equals(typeOfAccount))));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));

        TypedQuery<Account> query = toTypedQuery(cq);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Account> accounts = query.getResultList();

        // Count query
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<Account> countRoot = countCq.from(clazz);
        Join<Object, Object> countCurrency = countRoot.join("currency", JoinType.LEFT);
        Join<Object, Object> countBranch = countRoot.join("branch", JoinType.LEFT);
        List<Predicate> countPreds = new ArrayList<>();

        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.toLowerCase() + "%";
            countPreds.add(cb.or(
                    cb.like(cb.lower(countRoot.get("number")), pattern),
                    cb.like(cb.lower(countRoot.get("name")), pattern),
                    cb.like(cb.lower(countCurrency.get("name")), pattern),
                    cb.like(cb.lower(countBranch.get("name")), pattern)
            ));
        }
        if (currencyId != null) {
            countPreds.add(cb.equal(countCurrency.get("id"), currencyId));
        }
        if (accountTypes != null && !accountTypes.isEmpty()) {
            countPreds.add(countRoot.get("type").in(accountTypes));
        }
        if (typeOfAccount != null) {
            countPreds.add(cb.equal(countRoot.get("isDebit"), (AccountRequest.TypeOfAccount.DEBIT.equals(typeOfAccount))));
        }

        countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        return new PageImpl<>(accounts, pageable, total);
    }

    @Override
    public Page<Account> searchCurrentAccounts(long accountTagId, String searchString, AccountType accountType, Pageable pageable) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<AccountExtendedTag> cq = cb.createQuery(AccountExtendedTag.class);
        Root<AccountExtendedTag> root = cq.from(AccountExtendedTag.class);
        Join<AccountExtendedTag, Account> accountJoin = root.join("account");

        List<Predicate> preds = new ArrayList<>();
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.toLowerCase() + "%";
            preds.add(cb.or(
                    cb.like(cb.lower(accountJoin.get("number")), pattern),
                    cb.like(cb.lower(accountJoin.get("name")), pattern)
            ));
        }
        if (accountType != null) {
            preds.add(cb.equal(accountJoin.get("type"), accountType));
        }
        preds.add(cb.equal(root.get("accountTag").get("id"), accountTagId));

        cq.select(root).where(preds.toArray(new Predicate[0]));

        TypedQuery<AccountExtendedTag> q = getEntityManager().createQuery(cq);
        q.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        q.setMaxResults(pageable.getPageSize());

        List<Account> accounts = new ArrayList<>();
        List<AccountExtendedTag> tags = q.getResultList();
        for (AccountExtendedTag t : tags) {
            accounts.add(t.getAccount());
        }

        // Count
        CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
        Root<AccountExtendedTag> countRoot = countCq.from(AccountExtendedTag.class);
        Join<AccountExtendedTag, Account> countAccountJoin = countRoot.join("account");
        List<Predicate> countPreds = new ArrayList<>();
        if (!StringUtils.isEmpty(searchString)) {
            String pattern = "%" + searchString.toLowerCase() + "%";
            countPreds.add(cb.or(
                    cb.like(cb.lower(countAccountJoin.get("number")), pattern),
                    cb.like(cb.lower(countAccountJoin.get("name")), pattern)
            ));
        }
        if (accountType != null) {
            countPreds.add(cb.equal(countAccountJoin.get("type"), accountType));
        }
        countPreds.add(cb.equal(countRoot.get("accountTag").get("id"), accountTagId));

        countCq.select(cb.countDistinct(countRoot)).where(countPreds.toArray(new Predicate[0]));
        Long total = getEntityManager().createQuery(countCq).getSingleResult();

        return new PageImpl<>(accounts, pageable, total);
    }

    @Override
    public List<Long> findAllAccountIdByParentId(Long parentId) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Account> root = cq.from(clazz);
        cq.select(root.get("id")).where(cb.equal(root.get("parent").get("id"), parentId));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public AccountType getAccountTypeByAccountId(Long accountId) {
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<AccountType> cq = cb.createQuery(AccountType.class);
        Root<Account> root = cq.from(clazz);
        cq.select(root.get("type")).where(cb.equal(root.get("id"), accountId));
        TypedQuery<AccountType> q = getEntityManager().createQuery(cq);
        List<AccountType> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }
}
