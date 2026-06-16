import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '../../core/guards/auth-guard.service';
import { ShareProductListComponent } from './product-list/share-product-list.component';
import { ShareProductFormComponent } from './product-form/share-product-form.component';
import { SharePurchaseComponent } from './purchase/share-purchase.component';
import { ShareTransferComponent } from './transfer/share-transfer.component';
import { ShareTransactionHistoryComponent } from './transaction-history/share-transaction-history.component';
import { MemberSharePortfolioComponent } from './member-portfolio/member-share-portfolio.component';
import { SaccoSharePortfolioComponent } from './portfolio-dashboard/sacco-share-portfolio.component';
import { ShareAgeAnalysisComponent } from './age-analysis/share-age-analysis.component';

const routes: Routes = [
  {
    path: 'shares/products',
    component: ShareProductListComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/products/create',
    component: ShareProductFormComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/products/:id/edit',
    component: ShareProductFormComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/purchase',
    component: SharePurchaseComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/transfer',
    component: ShareTransferComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/transactions',
    component: ShareTransactionHistoryComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/member-portfolio',
    component: MemberSharePortfolioComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/portfolio',
    component: SaccoSharePortfolioComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'shares/age-analysis',
    component: ShareAgeAnalysisComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SharesRoutingModule {
}
