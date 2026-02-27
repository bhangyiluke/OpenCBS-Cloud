import { VaultInfoActions } from './vault-info.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IVaultInfo extends IReduxBase {
}

export function vaultInfoReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new VaultInfoActions(), state, {type, payload});
}

