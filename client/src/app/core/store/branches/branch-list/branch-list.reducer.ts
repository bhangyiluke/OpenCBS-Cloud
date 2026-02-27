import { BranchListActions } from './branch-list.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IBranchList extends IReduxBase {
}

export function branchListReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BranchListActions(), state, {type, payload});
}

