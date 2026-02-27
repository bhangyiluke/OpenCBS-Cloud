import { of as observableOf, Observable } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {createEffect, Actions, ofType} from '@ngrx/effects';
import { Action } from '@ngrx/store';


import { PayeeCreateService } from './payee-create.service';
import * as payeeCreateActions from './payee-create.actions';


@Injectable()
export class PayeeCreateEffects {

  create_payee$ = createEffect(() => this.actions$
    .pipe(ofType(payeeCreateActions.CREATE_PAYEE),
    switchMap((action: payeeCreateActions.PayeeCreateActions) => {
      return this.payeeCreateService.createPayee(action.payload).pipe(
        map(
          res => {
            return new payeeCreateActions.CreatePayeeSuccess();
          }
        ),
        catchError((err): Observable<Action> => {
          const errObj = new payeeCreateActions.CreatePayeeFailure(err.error);
          return observableOf(errObj);
        }));
    })));

  constructor(private payeeCreateService: PayeeCreateService,
              private actions$: Actions) {
  }
}
