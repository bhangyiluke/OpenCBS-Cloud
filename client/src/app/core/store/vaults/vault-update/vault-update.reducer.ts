import { ReduxBaseReducer, IReduxBase } from '../../redux-base/redux.base.reducer';

import { VaultUpdateActions } from './vault-update.actions';
import { NgRxAction } from '../../action.interface';

export interface IUpdateVault extends IReduxBase {
}

export function vaultUpdateReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new VaultUpdateActions(), state, {type, payload});
}


