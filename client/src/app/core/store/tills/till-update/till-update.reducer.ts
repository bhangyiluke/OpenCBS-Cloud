import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { TillUpdateActions } from './till-update.actions';
import { NgRxAction } from '../../action.interface';

export interface IUpdateTill extends IReduxBase {
}

export function tillUpdateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new TillUpdateActions(), state, {type, payload});
}
