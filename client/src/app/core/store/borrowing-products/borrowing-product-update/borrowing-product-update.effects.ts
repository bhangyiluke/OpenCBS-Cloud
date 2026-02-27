import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { BorrowingProductUpdateActions } from './borrowing-product-update.actions';
import { BorrowingProductUpdateService } from './borrowing-product-update.service';
import { ReduxBaseEffects } from '../../redux-base';

@Injectable()
export class BorrowingProductUpdateEffects {

  update_borrowing_product$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.borrowingProductUpdateActions, (action) => {
    return this.borrowingProductUpdateService.updateBorrowingProduct(action.payload.data.loanProductForm,
      action.payload.data.loanProductId);
  })) as any);

  constructor(private borrowingProductUpdateService: BorrowingProductUpdateService,
              private borrowingProductUpdateActions: BorrowingProductUpdateActions,
              private actions$: Actions) {
  }
}
