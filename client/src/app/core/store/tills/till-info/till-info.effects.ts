import { Injectable } from '@angular/core';
import { TillInfoActions } from './till-info.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { TillInfoService } from './till-info.service';
import { ReduxBaseEffects } from '../../redux-base';
import { Observable, of } from 'rxjs';

@Injectable()
export class TillInfoEffects {
  load_till$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.tillInfoActions, (action) => {
    return this.tillInfoService.getTillInfo(action.payload.data);
  })) as any);

  constructor(private tillInfoService: TillInfoService,
              private tillInfoActions: TillInfoActions,
              private actions$: Actions) {
  }
}
