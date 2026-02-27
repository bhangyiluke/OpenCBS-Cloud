import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { VaultUpdateActions } from './vault-update.actions';
import { VaultUpdateService } from './vault-update.service';
import { ReduxBaseEffects } from '../../redux-base/redux.base.effects';

@Injectable()
export class VaultUpdateEffects {

  update_vault$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.vaultUpdateActions, (action) => {
    return this.vaultUpdateService.updateVault(action.payload.data.vault, action.payload.data.id);
  })) as any);

  constructor(private vaultUpdateService: VaultUpdateService,
              private vaultUpdateActions: VaultUpdateActions,
              private actions$: Actions) {
  }
}
