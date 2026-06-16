import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SharesService } from '../shared/shares.service';
import { ShareProduct } from '../shared/shares.models';

@Component({
  standalone: false,
  selector: 'cbs-share-product-list',
  templateUrl: 'share-product-list.component.html',
  styleUrls: ['share-product-list.component.scss']
})
export class ShareProductListComponent implements OnInit {

  public products: ShareProduct[] = [];
  public loading = false;
  public searchQuery = '';
  public queryObject = { search: '', page: 1, size: 20 };

  constructor(private sharesService: SharesService,
              private router: Router) {
  }

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading = true;
    this.sharesService.getShareProducts(this.queryObject).subscribe((response: any) => {
      this.products = response.content || [];
      this.loading = false;
    }, () => this.loading = false);
  }

  search(query?) {
    this.queryObject.search = query || '';
    this.queryObject.page = 1;
    this.loadProducts();
  }

  createProduct() {
    this.router.navigate(['/shares/products/create']);
  }

  editProduct(product: ShareProduct) {
    this.router.navigate(['/shares/products', product.id, 'edit']);
  }

  activate(product: ShareProduct) {
    this.sharesService.activateShareProduct(product.id).subscribe(() => this.loadProducts());
  }

  deactivate(product: ShareProduct) {
    this.sharesService.deactivateShareProduct(product.id).subscribe(() => this.loadProducts());
  }
}
