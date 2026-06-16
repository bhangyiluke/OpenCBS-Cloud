package com.opencbs.shares.services;

import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.request.domain.RequestType;
import com.opencbs.core.services.CrudService;
import com.opencbs.core.services.audit.HistoryService;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.dto.ShareProductListFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ShareProductService extends CrudService<ShareProduct>, HistoryService {

    ShareProduct save(ShareProduct shareProduct);

    Optional<ShareProduct> getOne(Long id);

    Optional<ShareProduct> findByName(String name);

    Optional<ShareProduct> findByCode(String code);

    Page<ShareProduct> search(Pageable pageable, ShareProductListFilterDto filter);

    Page<ShareProduct> getActiveProducts(Pageable pageable, String search);

    ShareProduct activate(Long id);

    ShareProduct deactivate(Long id);

    Boolean isRequestSupported(RequestType requestType);
}
