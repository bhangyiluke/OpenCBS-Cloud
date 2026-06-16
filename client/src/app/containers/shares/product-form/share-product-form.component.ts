import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { SharesService } from '../shared/shares.service';
import { ShareProduct } from '../shared/shares.models';
import { environment } from '../../../../environments/environment';

@Component({
  standalone: false,
  selector: 'cbs-share-product-form',
  templateUrl: 'share-product-form.component.html',
  styleUrls: ['share-product-form.component.scss']
})
export class ShareProductFormComponent implements OnInit {

  public form: FormGroup;
  public currencies: any[] = [];
  public lotSelectionPolicies = ['FIFO', 'LIFO'];
  public statusTypes = ['ACTIVE', 'INACTIVE'];
  public loading = false;
  public productId: number;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private httpClient: HttpClient,
              private sharesService: SharesService) {
    this.form = this.createForm();
  }

  ngOnInit() {
    this.loadCurrencies();
    this.productId = this.route.snapshot.params['id'];
    if (this.productId) {
      this.loadProduct();
    }
  }

  loadCurrencies() {
    this.httpClient.get<any[]>(`${environment.API_ENDPOINT}currencies`).subscribe(currencies => this.currencies = currencies);
  }

  createForm() {
    return this.formBuilder.group({
      name: ['', Validators.required],
      code: ['', Validators.required],
      currencyId: ['', Validators.required],
      nominalValue: [1, [Validators.required, Validators.min(0.01)]],
      unitPrice: [1, [Validators.required, Validators.min(0.01)]],
      minSharesPerMember: [1, [Validators.required, Validators.min(1)]],
      maxSharesPerMember: [null],
      allowMemberTransfers: [false],
      lotSelectionPolicy: ['FIFO', Validators.required],
      statusType: ['ACTIVE', Validators.required]
    });
  }

  loadProduct() {
    this.sharesService.getShareProduct(this.productId).subscribe((product: ShareProduct) => {
      this.form.patchValue({
        name: product.name,
        code: product.code,
        currencyId: product.currency?.id,
        nominalValue: product.nominalValue,
        unitPrice: product.unitPrice,
        minSharesPerMember: product.minSharesPerMember,
        maxSharesPerMember: product.maxSharesPerMember,
        allowMemberTransfers: product.allowMemberTransfers,
        lotSelectionPolicy: product.lotSelectionPolicy,
        statusType: product.statusType
      });
    });
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;
    const payload = Object.assign({}, this.form.value, { id: this.productId });
    this.sharesService.saveShareProduct(payload).subscribe(() => {
      this.loading = false;
      this.router.navigate(['/shares/products']);
    }, () => this.loading = false);
  }

  cancel() {
    this.router.navigate(['/shares/products']);
  }
}
