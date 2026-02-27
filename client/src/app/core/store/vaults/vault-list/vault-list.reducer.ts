import { VaultListActions } from './vault-list.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IVaultList extends IReduxBase {
}

export function vaultListReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new VaultListActions(), state, {type, payload});
}

