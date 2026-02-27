import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { BorrowingProductUpdateActions } from './borrowing-product-update.actions';
import { NgRxAction } from '../../action.interface';

export interface IUpdateBorrowingProduct extends IReduxBase {
}

export function borrowingProductUpdateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BorrowingProductUpdateActions(), state, {type, payload});
}
