import { ReduxBaseReducer, IReduxBase } from '../redux-base/redux.base.reducer';

import { ChartOfAccountUpdateActions } from './chart-of-accounts-update.actions';
import { NgRxAction } from '../action.interface';

export interface IUpdateChartOfAccount extends IReduxBase {
}

export function chartOfAccountUpdateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new ChartOfAccountUpdateActions(), state, {type, payload});
}
