import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';

import { BorrowingProductCreateActions } from './borrowing-product-create.actions';
import { BorrowingProductCreateService } from './borrowing-product-create.service';
import { ReduxBaseEffects } from '../../redux-base';

@Injectable()
export class BorrowingProductCreateEffects {

  create_borrowing_product$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.borrowingProductCreateActions, (action) => {
    return this.borrowingProductCreateService.createBorrowingProduct(action.payload.data);
  })) as any);

  constructor(private borrowingProductCreateService: BorrowingProductCreateService,
              private borrowingProductCreateActions: BorrowingProductCreateActions,
              private actions$: Actions) {
  }
}
