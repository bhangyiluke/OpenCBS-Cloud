import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SharesService } from '../shared/shares.service';

@Component({
  standalone: false,
  selector: 'cbs-share-transfer',
  templateUrl: 'share-transfer.component.html',
  styleUrls: ['share-transfer.component.scss']
})
export class ShareTransferComponent implements OnInit {

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
      const values: any = {};
      if (params['sourceProfileId']) {
        values.sourceProfileId = params['sourceProfileId'];
      }
      if (params['destinationProfileId']) {
        values.destinationProfileId = params['destinationProfileId'];
      }
      if (params['shareProductId']) {
        values.shareProductId = params['shareProductId'];
      }
      if (params['quantity']) {
        values.quantity = params['quantity'];
      }
      if (params['unitPrice']) {
        values.unitPrice = params['unitPrice'];
      }
      if (params['transactionDate']) {
        values.transactionDate = params['transactionDate'];
      }
      if (params['reason']) {
        values.reason = params['reason'];
      }
      if (params['lotId']) {
        values.lotId = params['lotId'];
      }
      if (Object.keys(values).length) {
        this.form.patchValue(values, { emitEvent: false });
        if (values.shareProductId) {
          this.productSelected();
        }
      }
    });
  }

  createForm() {
    return this.formBuilder.group({
      sourceProfileId: ['', Validators.required],
      destinationProfileId: ['', Validators.required],
      shareProductId: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
      unitPrice: ['', [Validators.required, Validators.min(0.01)]],
      transactionDate: [new Date().toISOString().slice(0, 10), Validators.required],
      reason: [''],
      lotId: [''],
      idempotencyKey: ['']
    });
  }

  loadProducts() {
    this.sharesService.getShareProducts({ size: 100, statusType: 'ACTIVE' }).subscribe((response: any) => {
      this.products = response.content || [];
      this.productSelected();
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
    this.sharesService.transferShares(this.form.value).subscribe(() => {
      this.loading = false;
      this.router.navigate(['/shares/transactions']);
    }, () => this.loading = false);
  }
}
