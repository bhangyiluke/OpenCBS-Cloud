package com.opencbs.shares.services.impl;

import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.request.domain.RequestType;
import com.opencbs.core.services.audit.BaseHistoryService;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.repositories.ShareProductRepository;
import com.opencbs.shares.services.ShareProductService;
import com.opencbs.shares.dto.ShareProductListFilterDto;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShareProductServiceImpl extends BaseHistoryService<ShareProduct, Long, Integer> implements ShareProductService {

    private final ShareProductRepository shareProductRepository;

    public ShareProductServiceImpl(@NonNull ShareProductRepository shareProductRepository) {
        super(shareProductRepository);
        this.shareProductRepository = shareProductRepository;
    }

    @Override
    public ShareProduct save(ShareProduct shareProduct) {
        return shareProductRepository.save(shareProduct);
    }

    @Override
    public Optional<ShareProduct> getOne(Long id) {
        return shareProductRepository.findById(id);
    }

    @Override
    public ShareProduct update(ShareProduct shareProduct) {
        return save(shareProduct);
    }

    @Override
    public ShareProduct create(ShareProduct shareProduct) {
        return save(shareProduct);
    }

    @Override
    public List<ShareProduct> findAll() {
        return shareProductRepository.findAll();
    }

    @Override
    public Optional<ShareProduct> findByName(String name) {
        return shareProductRepository.findByName(name);
    }

    @Override
    public Optional<ShareProduct> findByCode(String code) {
        return shareProductRepository.findByCode(code);
    }

    @Override
    public Page<ShareProduct> search(Pageable pageable, ShareProductListFilterDto filter) {
        String search = filter == null ? null : filter.getSearch();
        StatusType statusType = filter == null ? null : filter.getStatusType();
        return shareProductRepository.search(pageable, search, statusType);
    }

    @Override
    public Page<ShareProduct> getActiveProducts(Pageable pageable, String search) {
        return shareProductRepository.search(pageable, search, StatusType.ACTIVE);
    }

    @Override
    public ShareProduct activate(Long id) {
        ShareProduct product = getOne(id).orElseThrow(() -> new IllegalArgumentException("Share product is not found."));
        product.setStatusType(StatusType.ACTIVE);
        return save(product);
    }

    @Override
    public ShareProduct deactivate(Long id) {
        ShareProduct product = getOne(id).orElseThrow(() -> new IllegalArgumentException("Share product is not found."));
        product.setStatusType(StatusType.INACTIVE);
        return save(product);
    }

    @Override
    public Boolean isRequestSupported(RequestType requestType) {
        return false;
    }

    @Override
    public Class getTargetClass() {
        return ShareProduct.class;
    }
}
