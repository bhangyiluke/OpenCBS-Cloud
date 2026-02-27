import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';

import { TillCreateActions } from './till-create.actions';
import { TillCreateService } from './till-create.service';
import { ReduxBaseEffects } from '../../redux-base';

@Injectable()
export class TillCreateEffects {

  create_till$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.tillCreateActions, (action) => {
    return this.tillCreateService.createTill(action.payload.data);
  })) as any);

  constructor(private tillCreateService: TillCreateService,
              private tillCreateActions: TillCreateActions,
              private actions$: Actions) {
  }
}
