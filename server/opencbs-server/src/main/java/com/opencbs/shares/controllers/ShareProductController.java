package com.opencbs.shares.controllers;

import com.opencbs.core.domain.enums.ModuleType;
import com.opencbs.core.security.permissions.PermissionRequired;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.dto.ShareProductDetailsDto;
import com.opencbs.shares.dto.ShareProductDto;
import com.opencbs.shares.dto.ShareProductListFilterDto;
import com.opencbs.shares.mapper.ShareProductMapper;
import com.opencbs.shares.services.ShareProductService;
import com.opencbs.shares.validators.ShareProductValidator;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share-products")
public class ShareProductController {

    private final ShareProductService shareProductService;
    private final ShareProductMapper shareProductMapper;
    private final ShareProductValidator shareProductValidator;

    public ShareProductController(@NonNull ShareProductService shareProductService,
                                  @NonNull ShareProductMapper shareProductMapper,
                                  @NonNull ShareProductValidator shareProductValidator) {
        this.shareProductService = shareProductService;
        this.shareProductMapper = shareProductMapper;
        this.shareProductValidator = shareProductValidator;
    }

    @GetMapping
    public Page<ShareProductDetailsDto> search(ShareProductListFilterDto filter, Pageable pageable) {
        return shareProductService.search(pageable, filter).map(shareProductMapper::mapToDetailsDto);
    }

    @GetMapping("/{id}")
    public ShareProductDetailsDto get(@PathVariable Long id) {
        ShareProduct product = shareProductService.getOne(id)
                .orElseThrow(() -> new IllegalArgumentException("Share product is not found."));
        return shareProductMapper.mapToDetailsDto(product);
    }

    @PermissionRequired(name = "SHARE_PRODUCT_CREATE", moduleType = ModuleType.SHARES, description = "Create share product")
    @PostMapping
    public ShareProductDetailsDto create(@RequestBody ShareProductDto dto) {
        shareProductValidator.validateOnCreate(dto);
        return shareProductMapper.mapToDetailsDto(shareProductService.save(shareProductMapper.mapToEntity(dto)));
    }

    @PermissionRequired(name = "SHARE_PRODUCT_UPDATE", moduleType = ModuleType.SHARES, description = "Update share product")
    @PutMapping("/{id}")
    public ShareProductDetailsDto update(@PathVariable Long id, @RequestBody ShareProductDto dto) {
        shareProductService.getOne(id).orElseThrow(() -> new IllegalArgumentException("Share product is not found."));
        dto.setId(id);
        shareProductValidator.validateOnUpdate(dto);
        return shareProductMapper.mapToDetailsDto(shareProductService.save(shareProductMapper.mapToEntity(dto)));
    }

    @PermissionRequired(name = "SHARE_PRODUCT_ACTIVATE", moduleType = ModuleType.SHARES, description = "Activate share product")
    @PostMapping("/{id}/activate")
    public ShareProductDetailsDto activate(@PathVariable Long id) {
        return shareProductMapper.mapToDetailsDto(shareProductService.activate(id));
    }

    @PermissionRequired(name = "SHARE_PRODUCT_DEACTIVATE", moduleType = ModuleType.SHARES, description = "Deactivate share product")
    @PostMapping("/{id}/deactivate")
    public ShareProductDetailsDto deactivate(@PathVariable Long id) {
        return shareProductMapper.mapToDetailsDto(shareProductService.deactivate(id));
    }
}
