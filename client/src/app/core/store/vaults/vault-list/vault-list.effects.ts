import { Injectable } from '@angular/core';
import { VaultListActions } from './vault-list.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { VaultListService } from './vault-list.service';
import { ReduxBaseEffects } from '../../redux-base';
import { Observable, of } from 'rxjs';

@Injectable()
export class VaultListEffects {
  load_vaults$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.vaultListActions, (action) => {
    return this.vaultListService.getVaultList(action.payload.data);
  })) as any);

  constructor(private vaultListService: VaultListService,
              private vaultListActions: VaultListActions,
              private actions$: Actions) {
  }
}
