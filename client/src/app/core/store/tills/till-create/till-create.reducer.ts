import { TillCreateActions } from './till-create.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface ICreateTill extends IReduxBase {
}

export function tillCreateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new TillCreateActions(), state, {type, payload}
  )
}

