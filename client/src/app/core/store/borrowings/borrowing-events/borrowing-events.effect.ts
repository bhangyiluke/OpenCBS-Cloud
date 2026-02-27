import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { ReduxBaseEffects } from '../../redux-base';


import { BorrowingEventsActions } from './borrowing-events.actions';
import { BorrowingEventsService } from './borrowing-events.service';
import { Observable, of } from 'rxjs';


@Injectable()
export class BorrowingEventsEffects {

  load_borrowing_events$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.borrowingEventsActions, (action) => {
    return this.borrowingEventsService.getBorrowingEvents(action.payload.data.id, action.payload.data.status);
  })) as any);

  constructor(private borrowingEventsService: BorrowingEventsService,
              private borrowingEventsActions: BorrowingEventsActions,
              private actions$: Actions) {
  }
}
