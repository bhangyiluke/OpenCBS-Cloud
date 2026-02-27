import { TillListActions } from './till-list.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface ITillList extends IReduxBase {
}

export function tillListReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new TillListActions(), state, {type, payload});
}

