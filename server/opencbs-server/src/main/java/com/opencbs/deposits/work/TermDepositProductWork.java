package com.opencbs.deposits.work;

import com.opencbs.core.annotations.Work;
import com.opencbs.core.exceptions.ResourceNotFoundException;
import com.opencbs.deposits.domain.TermDepositProduct;
import com.opencbs.deposits.dto.TermDepositProductDetailsDto;
import com.opencbs.deposits.dto.TermDepositProductDto;
import com.opencbs.deposits.mapper.TermDepositProductMapper;
import com.opencbs.deposits.services.TermDepositProductsService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Work
public class TermDepositProductWork {

    private final TermDepositProductsService termDepositProductsService;
    private final TermDepositProductMapper termDepositProductMapper;


    //@Autowired
    public TermDepositProductWork(@NonNull TermDepositProductsService termDepositProductsService,
                                  @NonNull TermDepositProductMapper termDepositProductMapper) {
        this.termDepositProductsService = termDepositProductsService;
        this.termDepositProductMapper = termDepositProductMapper;
    }

    public Page getAll(Pageable pageable, String searchString, Boolean showAll) {
        if (showAll) {
            return this.termDepositProductsService.getAll(pageable, searchString).map(this.termDepositProductMapper::entityToSimplifiedDto);
        }

        return this.termDepositProductsService.getActiveTermDepositProduct(pageable, searchString).map(this.termDepositProductMapper::entityToSimplifiedDto);
    }

    public TermDepositProductDetailsDto getById(Long id) {
        return this.termDepositProductsService.getOne(id).map(this.termDepositProductMapper::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Term deposit product not found (ID=%d)", id)));
    }

    public TermDepositProduct create(@NonNull TermDepositProductDto termDepositProductDto) {
        TermDepositProduct termDepositProduct = this.termDepositProductMapper.mapToEntity(termDepositProductDto);
        termDepositProduct.setId(null);
        return termDepositProductsService.save(termDepositProduct);
    }

    public TermDepositProduct update(@NonNull TermDepositProductDto termDepositProductDto) {
        TermDepositProduct termDepositProduct = this.termDepositProductMapper.mapToEntity(termDepositProductDto);
        return this.termDepositProductsService.save(termDepositProduct);
    }

}
