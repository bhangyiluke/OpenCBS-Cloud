import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { NglModule } from 'ng-lightning';
import { CoreModule } from '../../core/core.module';
import { SharesRoutingModule } from './shares-routing.module';
import { SharesService } from './shared/shares.service';
import { ShareProductListComponent } from './product-list/share-product-list.component';
import { ShareProductFormComponent } from './product-form/share-product-form.component';
import { SharePurchaseComponent } from './purchase/share-purchase.component';
import { ShareTransferComponent } from './transfer/share-transfer.component';
import { ShareTransactionHistoryComponent } from './transaction-history/share-transaction-history.component';
import { MemberSharePortfolioComponent } from './member-portfolio/member-share-portfolio.component';
import { SaccoSharePortfolioComponent } from './portfolio-dashboard/sacco-share-portfolio.component';
import { ShareAgeAnalysisComponent } from './age-analysis/share-age-analysis.component';

@NgModule({
  imports: [
    CommonModule,
    CoreModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    TranslateModule,
    NglModule,
    SharesRoutingModule
  ],
  declarations: [
    ShareProductListComponent,
    ShareProductFormComponent,
    SharePurchaseComponent,
    ShareTransferComponent,
    ShareTransactionHistoryComponent,
    MemberSharePortfolioComponent,
    SaccoSharePortfolioComponent,
    ShareAgeAnalysisComponent
  ],
  providers: [
    SharesService
  ]
})
export class SharesModule {
}
