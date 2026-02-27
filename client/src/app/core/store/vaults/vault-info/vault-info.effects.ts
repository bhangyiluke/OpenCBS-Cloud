import { Injectable } from '@angular/core';
import { VaultInfoActions } from './vault-info.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { VaultInfoService } from './vault-info.service';
import { ReduxBaseEffects } from '../../redux-base/redux.base.effects';
import { Observable, of } from 'rxjs';

@Injectable()
export class VaultInfoEffects {
  load_vault$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.vaultInfoActions, (action) => {
    return this.vaultInfoService.getVaultInfo(action.payload.data);
  })) as any);

  constructor(private vaultInfoService: VaultInfoService,
              private vaultInfoActions: VaultInfoActions,
              private actions$: Actions) {
  }
}
