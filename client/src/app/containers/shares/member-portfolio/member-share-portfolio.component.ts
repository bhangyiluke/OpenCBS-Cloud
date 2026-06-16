import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SharesService } from '../shared/shares.service';
import { MemberSharePortfolio } from '../shared/shares.models';

@Component({
  standalone: false,
  selector: 'cbs-member-share-portfolio',
  templateUrl: 'member-share-portfolio.component.html',
  styleUrls: ['member-share-portfolio.component.scss']
})
export class MemberSharePortfolioComponent implements OnInit {

  public form: FormGroup;
  public portfolio: MemberSharePortfolio;
  public loading = false;

  constructor(private formBuilder: FormBuilder,
              private sharesService: SharesService) {
    this.form = this.formBuilder.group({
      profileId: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  loadPortfolio() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.sharesService.getMemberPortfolio(this.form.get('profileId').value).subscribe((portfolio: MemberSharePortfolio) => {
      this.portfolio = portfolio;
      this.loading = false;
    }, () => this.loading = false);
  }
}
