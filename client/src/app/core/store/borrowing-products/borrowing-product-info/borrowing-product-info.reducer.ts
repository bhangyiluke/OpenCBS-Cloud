import { BorrowingProductInfoActions } from './borrowing-product-info.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IBorrowingProductInfo extends IReduxBase {
}

export function borrowingProductInfoReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BorrowingProductInfoActions(), state, {type, payload});
}

