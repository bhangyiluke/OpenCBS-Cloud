import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';

import { LoanProductService } from './loan-product.service';
import * as loanProductActions from './loan-product.actions';
import { NgRxAction } from '../../action.interface';


@Injectable()
export class LoanProductEffects {

  get_loan_product$ = createEffect(() => this.actions$
    .pipe(ofType(loanProductActions.LOAD_LOAN_PRODUCT),
      switchMap((action: NgRxAction) => {
        return this.loanProductService.getLoanProduct(action.payload).pipe(
          map(res => {
            return new loanProductActions.LoadLoanProductSuccess(res);
          }),
          catchError(err => {
            const errObj = new loanProductActions.LoadLoanProductFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(
    private loanProductService: LoanProductService,
    private actions$: Actions) {
  }
}
