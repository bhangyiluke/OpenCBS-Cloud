import { Component, OnInit } from '@angular/core';
import { SharesService } from '../shared/shares.service';

@Component({
  standalone: false,
  selector: 'cbs-share-transaction-history',
  templateUrl: 'share-transaction-history.component.html',
  styleUrls: ['share-transaction-history.component.scss']
})
export class ShareTransactionHistoryComponent implements OnInit {

  public transactions: any[] = [];
  public loading = false;
  public queryObject = { page: 1, size: 20 };

  constructor(private sharesService: SharesService) {
  }

  ngOnInit() {
    this.loadTransactions();
  }

  loadTransactions() {
    this.loading = true;
    this.sharesService.getShareTransactions(this.queryObject).subscribe((response: any) => {
      this.transactions = response.content || [];
      this.loading = false;
    }, () => this.loading = false);
  }
}
