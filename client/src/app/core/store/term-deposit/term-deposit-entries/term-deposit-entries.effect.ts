import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { ReduxBaseEffects } from '../../redux-base';


import { TermDepositEntriesActions } from './term-deposit-entries.actions';
import { TermDepositEntriesService } from './term-deposit-entries.service';
import { Observable, of } from 'rxjs';


@Injectable()
export class TermDepositEntriesEffects {

  load_term_deposit_entries$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.termDepositEntriesActions, (action) => {
    return this.termDepositEntriesService.getTermDepositEntries(action.payload.data.id, action.payload.data.page);
  })) as any);

  constructor(private termDepositEntriesService: TermDepositEntriesService,
              private termDepositEntriesActions: TermDepositEntriesActions,
              private actions$: Actions) {
  }
}
