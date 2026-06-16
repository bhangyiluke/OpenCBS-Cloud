import { Component, OnInit } from '@angular/core';
import { SharesService } from '../shared/shares.service';
import { SaccoSharePortfolio } from '../shared/shares.models';

@Component({
  standalone: false,
  selector: 'cbs-sacco-share-portfolio',
  templateUrl: 'sacco-share-portfolio.component.html',
  styleUrls: ['sacco-share-portfolio.component.scss']
})
export class SaccoSharePortfolioComponent implements OnInit {

  public portfolio: SaccoSharePortfolio;
  public loading = false;

  constructor(private sharesService: SharesService) {
  }

  ngOnInit() {
    this.loadPortfolio();
  }

  loadPortfolio() {
    this.loading = true;
    this.sharesService.getSaccoPortfolio().subscribe((portfolio: SaccoSharePortfolio) => {
      this.portfolio = portfolio;
      this.loading = false;
    }, () => this.loading = false);
  }
}
