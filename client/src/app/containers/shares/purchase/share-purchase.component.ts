import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SharesService } from '../shared/shares.service';

@Component({
  standalone: false,
  selector: 'cbs-share-purchase',
  templateUrl: 'share-purchase.component.html',
  styleUrls: ['share-purchase.component.scss']
})
export class SharePurchaseComponent implements OnInit {

  public form: FormGroup;
  public products: any[] = [];
  public loading = false;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private sharesService: SharesService) {
    this.form = this.createForm();
  }

  ngOnInit() {
    this.loadProducts();
    this.route.queryParams.subscribe(params => {
      if (params['profileId']) {
        this.form.patchValue({ profileId: params['profileId'] }, { emitEvent: false });
      }
    });
  }

  createForm() {
    return this.formBuilder.group({
      profileId: ['', Validators.required],
      shareProductId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      unitPrice: ['', [Validators.required, Validators.min(0.01)]],
      transactionDate: [new Date().toISOString().slice(0, 10), Validators.required],
      idempotencyKey: ['']
    });
  }

  loadProducts() {
    this.sharesService.getShareProducts({ size: 100, statusType: 'ACTIVE' }).subscribe((response: any) => {
      this.products = response.content || [];
    });
  }

  productSelected() {
    const product = this.products.find(item => item.id === Number(this.form.get('shareProductId').value));
    if (product) {
      this.form.patchValue({ unitPrice: product.unitPrice });
    }
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.sharesService.purchaseShares(this.form.value).subscribe(() => {
      this.loading = false;
      this.router.navigate(['/shares/transactions']);
    }, () => this.loading = false);
  }
}
