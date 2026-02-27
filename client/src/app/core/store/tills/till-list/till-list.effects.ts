import { Injectable } from '@angular/core';
import { TillListActions } from './till-list.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { TillListService } from './till-list.service';
import { ReduxBaseEffects } from '../../redux-base';
import { Observable, of } from 'rxjs';

@Injectable()
export class TillListEffects {
  load_tills$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.tillListActions, (action) => {
    return this.tillListService.getTillList(action.payload.data);
  })) as any);

  constructor(private tillListService: TillListService,
              private tillListActions: TillListActions,
              private actions$: Actions) {
  }
}
