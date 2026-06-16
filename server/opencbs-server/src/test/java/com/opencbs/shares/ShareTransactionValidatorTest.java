package com.opencbs.shares;

import com.opencbs.core.domain.Currency;
import com.opencbs.core.domain.enums.StatusType;
import com.opencbs.core.domain.profiles.Profile;
import com.opencbs.shares.domain.ShareProduct;
import com.opencbs.shares.domain.enums.ShareLotSelectionPolicy;
import com.opencbs.shares.domain.enums.ShareLotStatus;
import com.opencbs.shares.dto.PurchaseShareDto;
import com.opencbs.shares.dto.TransferShareDto;
import com.opencbs.shares.repositories.ShareLotRepository;
import com.opencbs.shares.services.ShareProductService;
import com.opencbs.shares.validators.ShareTransactionValidator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShareTransactionValidatorTest {

    @Test
    void rejectsPurchaseBelowMinimum() {
        ShareProductService productService = mock(ShareProductService.class);
        ProfileServiceStub profileService = new ProfileServiceStub();
        ShareLotRepository lotRepository = mock(ShareLotRepository.class);
        ShareTransactionValidator validator = new ShareTransactionValidator(productService, profileService, lotRepository);

        ShareProduct product = product();
        when(productService.getOne(1L)).thenReturn(Optional.of(product));
        when(lotRepository.sumQuantityByProfileAndProduct(10L, 1L, ShareLotStatus.ACTIVE)).thenReturn(0L);

        PurchaseShareDto dto = new PurchaseShareDto();
        dto.setProfileId(10L);
        dto.setShareProductId(1L);
        dto.setQuantity(1L);
        dto.setUnitPrice(product.getUnitPrice());

        assertThrows(IllegalArgumentException.class, () -> validator.validatePurchase(dto));
    }

    @Test
    void rejectsTransferToSameMember() {
        ShareProductService productService = mock(ShareProductService.class);
        ProfileServiceStub profileService = new ProfileServiceStub();
        ShareLotRepository lotRepository = mock(ShareLotRepository.class);
        ShareTransactionValidator validator = new ShareTransactionValidator(productService, profileService, lotRepository);

        ShareProduct product = product();
        product.setAllowMemberTransfers(true);
        when(productService.getOne(1L)).thenReturn(Optional.of(product));

        TransferShareDto dto = new TransferShareDto();
        dto.setSourceProfileId(10L);
        dto.setDestinationProfileId(10L);
        dto.setShareProductId(1L);
        dto.setQuantity(10L);
        dto.setUnitPrice(product.getUnitPrice());

        assertThrows(IllegalArgumentException.class, () -> validator.validateTransfer(dto));
    }

    private ShareProduct product() {
        ShareProduct product = new ShareProduct();
        product.setId(1L);
        product.setCurrency(new Currency());
        product.setName("Ordinary Shares");
        product.setCode("ORD");
        product.setNominalValue(BigDecimal.ONE);
        product.setUnitPrice(BigDecimal.TEN);
        product.setMinSharesPerMember(10L);
        product.setMaxSharesPerMember(1000L);
        product.setAllowMemberTransfers(false);
        product.setLotSelectionPolicy(ShareLotSelectionPolicy.FIFO);
        product.setStatusType(StatusType.ACTIVE);
        return product;
    }

    private static class ProfileServiceStub extends com.opencbs.core.services.ProfileService {
        ProfileServiceStub() {
            super(null, null, null);
        }

        @Override
        public Optional<Profile> findOne(Long profileId) {
            Profile profile = new Profile();
            profile.setId(profileId);
            profile.setName("Member " + profileId);
            return Optional.of(profile);
        }
    }
}
