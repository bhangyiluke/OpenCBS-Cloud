package com.opencbs.shares.validators;

import com.opencbs.core.annotations.Validator;
import com.opencbs.core.domain.Currency;
import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.services.CurrencyService;
import com.opencbs.core.validators.BaseValidator;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.domain.enums.ShareLotSelectionPolicy;
import com.opencbs.shares.dto.ShareProductDto;
import com.opencbs.shares.services.ShareProductService;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Optional;

@Validator
public class ShareProductValidator extends BaseValidator {

    private final ShareProductService shareProductService;
    private final CurrencyService currencyService;

    public ShareProductValidator(@NonNull ShareProductService shareProductService,
                                 @NonNull CurrencyService currencyService) {
        this.shareProductService = shareProductService;
        this.currencyService = currencyService;
    }

    public void validateOnCreate(ShareProductDto dto) {
        this.stringIsNotEmpty(dto.getName(), "Share product name is required.");
        Assert.isTrue(!this.shareProductService.findByName(dto.getName()).isPresent(), "Share product name is taken.");
        this.stringIsNotEmpty(dto.getCode(), "Share product code is required.");
        Assert.isTrue(!this.shareProductService.findByCode(dto.getCode()).isPresent(), "Share product code is taken.");
        this.validate(dto);
    }

    public void validateOnUpdate(ShareProductDto dto) {
        this.stringIsNotEmpty(dto.getName(), "Share product name is required.");
        Optional<ShareProduct> byName = this.shareProductService.findByName(dto.getName());
        if (byName.isPresent()) {
            Assert.isTrue(byName.get().getId().equals(dto.getId()), "Share product name is taken.");
        }
        this.stringIsNotEmpty(dto.getCode(), "Share product code is required.");
        Optional<ShareProduct> byCode = this.shareProductService.findByCode(dto.getCode());
        if (byCode.isPresent()) {
            Assert.isTrue(byCode.get().getId().equals(dto.getId()), "Share product code is taken.");
        }
        this.validate(dto);
    }

    private void validate(ShareProductDto dto) {
        Assert.notNull(dto.getCurrencyId(), "Currency is required.");
        Optional<Currency> currency = this.currencyService.findOne(dto.getCurrencyId());
        Assert.isTrue(currency.isPresent(), "Currency is not found.");

        Assert.notNull(dto.getNominalValue(), "Nominal value is required.");
        Assert.notNull(dto.getUnitPrice(), "Unit price is required.");
        Assert.notNull(dto.getMinSharesPerMember(), "Minimum shares per member is required.");
        Assert.notNull(dto.getAllowMemberTransfers(), "Member transfer flag is required.");
        Assert.notNull(dto.getLotSelectionPolicy(), "Lot selection policy is required.");
        Assert.notNull(dto.getStatusType(), "Status is required.");

        Assert.isTrue(dto.getNominalValue().compareTo(BigDecimal.ZERO) > 0, "Nominal value must be greater than zero.");
        Assert.isTrue(dto.getUnitPrice().compareTo(BigDecimal.ZERO) > 0, "Unit price must be greater than zero.");
        Assert.isTrue(dto.getMinSharesPerMember() > 0, "Minimum shares per member must be greater than zero.");
        Assert.isTrue(ShareLotSelectionPolicy.FIFO.equals(dto.getLotSelectionPolicy()) || ShareLotSelectionPolicy.LIFO.equals(dto.getLotSelectionPolicy()), "Invalid lot selection policy.");
        Assert.isTrue(StatusType.ACTIVE.equals(dto.getStatusType()) || StatusType.INACTIVE.equals(dto.getStatusType()), "Invalid status.");

        if (dto.getMaxSharesPerMember() != null) {
            Assert.isTrue(dto.getMaxSharesPerMember() >= dto.getMinSharesPerMember(), "Maximum shares per member must be greater than or equal to minimum shares per member.");
        }
    }
}
