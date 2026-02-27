import { ChartOfAccountCreateActions } from './chart-of-account-create.actions';
import { ReduxBaseReducer, IReduxBase } from '../redux-base';

import { NgRxAction } from '../action.interface';

export interface ICreateChartOfAccount extends IReduxBase {
}

export function chartOfAccountCreateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new ChartOfAccountCreateActions(), state, {type, payload});
}

