import { BranchInfoActions } from './branch-info.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IBranchInfo extends IReduxBase {
}

export function branchInfoReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BranchInfoActions(), state, {type, payload});
}

