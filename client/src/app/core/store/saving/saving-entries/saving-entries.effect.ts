import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { ReduxBaseEffects } from '../../redux-base';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import { SavingEntriesActions } from './saving-entries.actions';
import { SavingEntriesService } from './saving-entries.service';
import { Observable, of } from 'rxjs';


@Injectable()
export class SavingEntriesEffects {

  load_saving_entries$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.savingEntriesActions, (action) => {
    return this.savingEntriesService.getSavingEntries(action.payload.data.id, action.payload.data.page);
  })) as any);

  constructor(private savingEntriesService: SavingEntriesService,
              private savingEntriesActions: SavingEntriesActions,
              private actions$: Actions) {
  }
}
