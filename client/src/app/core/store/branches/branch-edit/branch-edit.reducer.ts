import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { BranchUpdateActions } from './branch-edit.actions';
import { NgRxAction } from '../../action.interface';

export interface IUpdateBranch extends IReduxBase {
}

export function branchUpdateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BranchUpdateActions(), state, {type, payload});
}
