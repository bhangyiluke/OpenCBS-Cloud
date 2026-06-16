package com.opencbs.shares.controllers;

import com.opencbs.core.domain.enums.ModuleType;
import com.opencbs.core.helpers.UserHelper;
import com.opencbs.core.security.permissions.PermissionRequired;
import com.opencbs.shares.domain.ShareLot;
import com.opencbs.shares.domain.ShareTransaction;
import com.opencbs.shares.dto.*;
import com.opencbs.shares.mapper.ShareMapper;
import com.opencbs.shares.services.ShareTransactionService;
import com.opencbs.shares.validators.ShareTransactionValidator;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shares")
public class ShareController {

    private final ShareTransactionService shareTransactionService;
    private final ShareTransactionValidator shareTransactionValidator;
    private final ShareMapper shareMapper;

    public ShareController(@NonNull ShareTransactionService shareTransactionService,
                           @NonNull ShareTransactionValidator shareTransactionValidator,
                           @NonNull ShareMapper shareMapper) {
        this.shareTransactionService = shareTransactionService;
        this.shareTransactionValidator = shareTransactionValidator;
        this.shareMapper = shareMapper;
    }

    @PermissionRequired(name = "SHARE_PURCHASE", moduleType = ModuleType.SHARES, description = "Purchase shares")
    @PostMapping("/purchases")
    public ShareTransactionDto purchase(@RequestBody PurchaseShareDto dto) {
        shareTransactionValidator.validatePurchase(dto);
        return shareMapper.mapToShareTransactionDto(shareTransactionService.purchaseShares(dto, UserHelper.getCurrentUser()));
    }

    @PermissionRequired(name = "SHARE_TRANSFER", moduleType = ModuleType.SHARES, description = "Transfer shares")
    @PostMapping("/transfers")
    public ShareTransactionDto transfer(@RequestBody TransferShareDto dto) {
        shareTransactionValidator.validateTransfer(dto);
        return shareMapper.mapToShareTransactionDto(shareTransactionService.transferShares(dto, UserHelper.getCurrentUser()));
    }

    @GetMapping("/transactions")
    public Page<ShareTransactionDto> transactions(ShareTransactionFilterDto filter, Pageable pageable) {
        return shareTransactionService.searchTransactions(filter, pageable).map(shareMapper::mapToShareTransactionDto);
    }

    @GetMapping("/members/{profileId}/lots")
    public Page<ShareLotDto> memberLots(@PathVariable Long profileId,
                                        @RequestParam(value = "shareProductId", required = false) Long shareProductId,
                                        Pageable pageable) {
        return shareTransactionService.searchLots(profileId, shareProductId, pageable)
                .map(lot -> shareMapper.mapToShareLotDto(lot, java.time.LocalDate.now()));
    }

    @GetMapping("/members/{profileId}/portfolio")
    public MemberSharePortfolioDto memberPortfolio(@PathVariable Long profileId) {
        return shareTransactionService.memberPortfolio(profileId);
    }

    @GetMapping("/portfolio/sacco")
    public SaccoSharePortfolioDto saccoPortfolio(SharePortfolioFilterDto filter) {
        return shareTransactionService.saccoPortfolio(filter);
    }

    @GetMapping("/portfolio/age-analysis")
    public ShareAgeAnalysisDto ageAnalysis(ShareAgeAnalysisFilterDto filter) {
        return shareTransactionService.analyzeAge(filter);
    }
}
