import { ReduxBaseReducer, IReduxBase } from '../redux-base/redux.base.reducer';

import { ChartOfAccountsActions } from './chart-of-accounts.actions';
import { NgRxAction } from '../action.interface';

export interface IChartOfAccounts extends IReduxBase {
}

export function chartOfAccountsReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new ChartOfAccountsActions(), state, {type, payload});
}
