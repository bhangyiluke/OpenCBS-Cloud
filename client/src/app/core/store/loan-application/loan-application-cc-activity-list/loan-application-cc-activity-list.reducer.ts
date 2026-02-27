import { LoanAppCCActivityListActions } from './loan-application-cc-activity-list.actions';
import { IReduxBase, ReduxBaseReducer } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface ILoanAppCCActivityList extends IReduxBase {
}

export function loanAppCCActivityListReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new LoanAppCCActivityListActions(), state, {type, payload});
}


