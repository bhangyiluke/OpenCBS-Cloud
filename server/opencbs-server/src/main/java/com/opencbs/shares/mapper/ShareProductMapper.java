package com.opencbs.shares.mapper;

import com.opencbs.core.domain.Currency;
import com.opencbs.core.dto.CurrencyDto;
import com.opencbs.core.services.CurrencyService;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.dto.ShareProductDetailsDto;
import com.opencbs.shares.dto.ShareProductDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareProductMapper {

    private final CurrencyService currencyService;

    public ShareProductMapper(@NonNull CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public ShareProduct mapToEntity(ShareProductDto dto) {
        Currency currency = this.currencyService.findOne(dto.getCurrencyId())
                .orElseThrow(() -> new IllegalArgumentException("Currency is not found."));
        ShareProduct product = new ShareProduct();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setCode(dto.getCode());
        product.setCurrency(currency);
        product.setNominalValue(dto.getNominalValue());
        product.setUnitPrice(dto.getUnitPrice());
        product.setMinSharesPerMember(dto.getMinSharesPerMember());
        product.setMaxSharesPerMember(dto.getMaxSharesPerMember());
        product.setAllowMemberTransfers(dto.getAllowMemberTransfers());
        product.setLotSelectionPolicy(dto.getLotSelectionPolicy());
        product.setStatusType(dto.getStatusType());
        return product;
    }

    public ShareProductDetailsDto mapToDetailsDto(ShareProduct product) {
        ShareProductDetailsDto dto = new ShareProductDetailsDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCode(product.getCode());
        dto.setCurrency(this.mapCurrency(product.getCurrency()));
        dto.setNominalValue(product.getNominalValue());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setMinSharesPerMember(product.getMinSharesPerMember());
        dto.setMaxSharesPerMember(product.getMaxSharesPerMember());
        dto.setAllowMemberTransfers(product.getAllowMemberTransfers());
        dto.setLotSelectionPolicy(product.getLotSelectionPolicy());
        dto.setStatusType(product.getStatusType());
        return dto;
    }

    public List<ShareProductDetailsDto> mapToDetailsDto(List<ShareProduct> products) {
        return products.stream()
                .map(this::mapToDetailsDto)
                .collect(Collectors.toList());
    }

    private CurrencyDto mapCurrency(Currency currency) {
        CurrencyDto dto = new CurrencyDto();
        dto.setId(currency.getId());
        dto.setName(currency.getName());
        dto.setCode(currency.getCode());
        dto.setMain(currency.isMain());
        return dto;
    }
}
