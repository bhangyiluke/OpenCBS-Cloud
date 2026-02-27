import { ProfileChangeActions } from './profile-changes.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IProfileChanges extends IReduxBase {
}

export function profileChangesReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new ProfileChangeActions(), state, {type, payload}
  );
}

