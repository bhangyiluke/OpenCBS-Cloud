package com.opencbs.shares.services;

import com.opencbs.core.domain.User;
import com.opencbs.shares.domain.ShareLot;
import com.opencbs.shares.domain.ShareTransaction;
import com.opencbs.shares.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShareTransactionService {

    ShareTransaction purchaseShares(PurchaseShareDto dto, User actor);

    ShareTransaction transferShares(TransferShareDto dto, User actor);

    Page<ShareTransaction> searchTransactions(ShareTransactionFilterDto filter, Pageable pageable);

    Page<ShareLot> searchLots(Long profileId, Long shareProductId, Pageable pageable);

    ShareAgeAnalysisDto analyzeAge(ShareAgeAnalysisFilterDto filter);

    SaccoSharePortfolioDto saccoPortfolio(SharePortfolioFilterDto filter);

    MemberSharePortfolioDto memberPortfolio(Long profileId);
}
