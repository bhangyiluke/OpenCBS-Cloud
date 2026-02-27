import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import * as loanApplicationUpdateActions from './loan-application-update.actions';
import { LoanApplicationUpdateService } from './loan-application-update.service';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class LoanApplicationUpdateEffects {

  update_loanApplication$ = createEffect(() => this.actions$
    .pipe(ofType(loanApplicationUpdateActions.UPDATE_LOAN_APPLICATION),
      switchMap((action: NgRxAction) => {
        return this.loanApplicationUpdateService.updateLoanApplication(action.payload.data, action.payload.loanAppId).pipe(
          map(
            res => new loanApplicationUpdateActions.UpdateLoanApplicationSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new loanApplicationUpdateActions.UpdateLoanApplicationFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  disburse_loan_payee$ = createEffect(() => this.actions$
    .pipe(ofType(loanApplicationUpdateActions.DISBURSE_LOAN_PAYEE),
      switchMap((action: NgRxAction) => {
        return this.loanApplicationUpdateService.disbursePayee(action.payload.loanAppId, action.payload.payeeId, action.payload.data).pipe(
          map(res => new loanApplicationUpdateActions.UpdateLoanApplicationSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new loanApplicationUpdateActions.UpdateLoanApplicationFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(private loanApplicationUpdateService: LoanApplicationUpdateService,
              private actions$: Actions) {
  }
}
