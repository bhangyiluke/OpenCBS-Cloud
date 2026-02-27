import { SavingEntriesActions } from './saving-entries.actions';
import { IReduxBase, ReduxBaseReducer } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface ISavingEntries extends IReduxBase {
}

export function savingEntriesReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new SavingEntriesActions(), state, {type, payload})
}
