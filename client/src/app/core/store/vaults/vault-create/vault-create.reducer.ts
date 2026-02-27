import { VaultCreateActions } from './vault-create.actions';
import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface ICreateVault extends IReduxBase {
}

export function vaultCreateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new VaultCreateActions(), state, {type, payload});
}
