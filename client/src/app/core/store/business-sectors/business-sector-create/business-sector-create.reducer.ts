import { BusinessSectorCreateActions } from './business-sector-create.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface ICreateBusinessSector extends IReduxBase {
}

export function businessSectorCreateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BusinessSectorCreateActions(), state, {type, payload});
}

