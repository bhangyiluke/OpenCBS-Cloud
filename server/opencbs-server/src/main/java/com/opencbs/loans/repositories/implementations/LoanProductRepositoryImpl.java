package com.opencbs.loans.repositories.implementations;

import com.opencbs.core.repositories.implementations.BaseRepository;
import com.opencbs.loans.domain.products.LoanProduct;
import com.opencbs.loans.dto.requests.LoanProductRequest;
import com.opencbs.loans.repositories.customs.LoanProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class LoanProductRepositoryImpl extends BaseRepository<LoanProduct> implements LoanProductRepositoryCustom {

    public LoanProductRepositoryImpl(EntityManager entityManager) {
        super(entityManager, LoanProduct.class);
    }


    @Override
    public Page<LoanProduct> search(Pageable pageable, LoanProductRequest request) {
        String searchString = StringUtils.isEmpty(request.getSearch()) ? "" : request.getSearch();
        CriteriaBuilder cb = criteriaBuilder();
        CriteriaQuery<LoanProduct> cq = cb.createQuery(LoanProduct.class);
        Root<LoanProduct> root = cq.from(LoanProduct.class);

        List<Predicate> preds = new ArrayList<>();
        if (!searchString.isEmpty()) {
            String pattern = "%" + searchString.toLowerCase() + "%";
            preds.add(cb.or(
                cb.like(cb.lower(root.get("name")), pattern),
                cb.like(cb.lower(root.get("code")), pattern)
            ));
        }
        if (!request.getStatusTypes().isEmpty()) {
            preds.add(root.get("statusType").in(request.getStatusTypes()));
        }

        cq.select(root).where(preds.toArray(new Predicate[0]));

        List<LoanProduct> all = getEntityManager().createQuery(cq).getResultList();

        List<LoanProduct> filtered = all.stream()
            .filter(x -> (x.getAvailability() & request.getAvailability().getId()) == request.getAvailability().getId())
            .collect(Collectors.toList());

        int start = Math.toIntExact(pageable.getPageNumber() * pageable.getPageSize());
        List<LoanProduct> results = filtered.stream()
            .skip(start)
            .limit(pageable.getPageSize())
            .collect(Collectors.toList());

        return new PageImpl<>(results, pageable, filtered.size());
    }
}
