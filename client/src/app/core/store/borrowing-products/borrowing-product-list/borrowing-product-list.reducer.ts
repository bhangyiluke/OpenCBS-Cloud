import { BorrowingProductListActions } from './borrowing-product-list.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base';

import { NgRxAction } from '../../action.interface';

export interface IBorrowingProductList extends IReduxBase {
}

export function borrowingProductListReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BorrowingProductListActions(), state, {type, payload});
}

