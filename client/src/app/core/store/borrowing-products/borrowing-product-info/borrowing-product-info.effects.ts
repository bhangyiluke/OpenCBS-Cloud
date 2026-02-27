import { Injectable } from '@angular/core';
import { BorrowingProductInfoActions } from './borrowing-product-info.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { BorrowingProductInfoService } from './borrowing-product-info.service';
import { ReduxBaseEffects } from '../../redux-base/redux.base.effects';
import { Observable, of } from 'rxjs';

@Injectable()
export class BorrowingProductInfoEffects {
  load_borrowing_product$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.borrowingProductInfoActions, (action) => {
    return this.borrowingProductInfoService.getBorrowingProductInfo(action.payload.data);
  })) as any);

  constructor(private borrowingProductInfoService: BorrowingProductInfoService,
              private borrowingProductInfoActions: BorrowingProductInfoActions,
              private actions$: Actions) {
  }
}
