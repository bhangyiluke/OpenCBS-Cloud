import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';


import { CreditLineUpdateService } from './credit-line-update.service';
import * as creditLineUpdateActions from './credit-line-update.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class CreditLineUpdateEffects {

  update_credit_line$ = createEffect(() => this.actions$
    .pipe(ofType(creditLineUpdateActions.UPDATE_CREDIT_LINE),
      switchMap((action: NgRxAction) => {
        return this.creditLineUpdateService.updateCreditLine(
          action.payload.creditLineForm,
          action.payload.creditLineId
        ).pipe(
          map(
            res => new creditLineUpdateActions.UpdateCreditLineSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new creditLineUpdateActions.UpdateCreditLineFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(private creditLineUpdateService: CreditLineUpdateService,
              private actions$: Actions) {
  }
}
