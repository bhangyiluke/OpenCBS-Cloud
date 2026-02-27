import { Injectable } from '@angular/core';
import { createEffect, Actions } from '@ngrx/effects';
import { ReduxBaseEffects } from '../redux-base';


import { OtherFeesActions } from './other-fees.actions';
import { OtherFeesService } from './other-fees.service';
import { Observable, of } from 'rxjs';


@Injectable()
export class OtherFeesEffects {

  load_other_fees$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.OtherFeesActions, (action) => {
    return this.OtherFeesService.getOtherFees(action.payload.data);
  })) as any);


  constructor(private OtherFeesService: OtherFeesService,
              private OtherFeesActions: OtherFeesActions,
              private actions$: Actions) {
  }
}
