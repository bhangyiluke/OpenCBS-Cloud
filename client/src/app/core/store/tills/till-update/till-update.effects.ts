import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { TillUpdateActions } from './till-update.actions';
import { TillUpdateService } from './till-update.service';
import { ReduxBaseEffects } from '../../redux-base';

@Injectable()
export class TillUpdateEffects {

  update_till$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.tillUpdateActions, (action) => {
    return this.tillUpdateService.updateTill(action.payload.data.till, action.payload.data.id);
  })) as any);

  constructor(private tillUpdateService: TillUpdateService,
              private tillUpdateActions: TillUpdateActions,
              private actions$: Actions) {
  }
}
