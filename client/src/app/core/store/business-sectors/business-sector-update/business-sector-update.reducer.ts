import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { BusinessSectorUpdateActions } from './business-sector-update.actions';
import { NgRxAction } from '../../action.interface';

export interface IUpdateBusinessSector extends IReduxBase {
}

export function businessSectorUpdateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BusinessSectorUpdateActions(), state, {type, payload});
}
