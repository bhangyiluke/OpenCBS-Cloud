import { BusinessSectorListActions } from './business-sector-list.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IBusinessSectorList extends IReduxBase {
}

export function businessSectorListReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BusinessSectorListActions(), state, {type, payload});
}

