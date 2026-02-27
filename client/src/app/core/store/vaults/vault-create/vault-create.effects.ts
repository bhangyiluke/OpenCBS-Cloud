import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';

import { VaultCreateActions } from './vault-create.actions';
import { VaultCreateService } from './vault-create.service';
import { ReduxBaseEffects } from '../../redux-base';

@Injectable()
export class VaultCreateEffects {

  create_vault$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.vaultCreateActions, (action) => {
    return this.vaultCreateService.createVault(action.payload.data);
  })) as any);

  constructor(private vaultCreateService: VaultCreateService,
              private vaultCreateActions: VaultCreateActions,
              private actions$: Actions) {
  }
}
