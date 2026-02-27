import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';

import { LoanMakerCheckerRepaymentService } from './loan-maker-checker-repayment.service';
import * as loanMakerCheckerRepaymentActions from './loan-maker-checker-repayment.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class LoanMakerCheckerRepaymentEffects {

  get_loan_maker_checker_repayment$ = createEffect(() => this.actions$
    .pipe(ofType(loanMakerCheckerRepaymentActions.LOAD_LOAN_MAKER_CHECKER_REPAYMENT),
      switchMap((action: NgRxAction) => {
        return this.loanMakerCheckerRepaymentService.getLoanMakerCheckerRepayment(action.payload).pipe(
          map(res => {
            return new loanMakerCheckerRepaymentActions.LoadLoanMakerCheckerRepaymentSuccess(res);
          }),
          catchError((err: HttpErrorResponse) => {
            const errObj = new loanMakerCheckerRepaymentActions.LoadLoanMakerCheckerRepaymentFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(
    private loanMakerCheckerRepaymentService: LoanMakerCheckerRepaymentService,
    private actions$: Actions) {
  }
}
