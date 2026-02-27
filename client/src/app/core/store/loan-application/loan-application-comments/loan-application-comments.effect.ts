import { Observable, of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';

import * as loanApplicationCommentsActions from './loan-application-comments.actions';
import { LoanApplicationCommentsService } from './loan-application-comments.service';
import { NgRxAction } from '../../action.interface';


@Injectable()
export class LoanApplicationCommentsEffects {
  load_loan_app_comments$= createEffect(() => this.actions$
    .pipe(ofType(loanApplicationCommentsActions.LOAD_LOAN_APPLICATION_COMMENTS),
      switchMap((action: NgRxAction) => {
        return this.loanApplicationCommentsListService.getLoanApplicationComments(action.payload).pipe(
          map(
            res => {
              return new loanApplicationCommentsActions.LoadLoanApplicationCommentsSuccess(res);
            }
          ),
          catchError(err => {
            const errObj = new loanApplicationCommentsActions.LoadLoanApplicationCommentsFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  set_loan_app_comments$ = createEffect(() => this.actions$
    .pipe(ofType(loanApplicationCommentsActions.SET_LOAN_APPLICATION_COMMENTS),
    switchMap((action: NgRxAction) => {
      return this.loanApplicationCommentsListService.addComment(action.payload)
        .pipe(
          map(
            res => {
              return new loanApplicationCommentsActions.LoadLoanApplicationCommentsSuccess(res);
            }
          ),
          catchError(err => {
            const errObj = new loanApplicationCommentsActions.LoadLoanApplicationCommentsFailure(err.error);
            return observableOf(errObj);
          }));
    })));

  constructor(private loanApplicationCommentsListService: LoanApplicationCommentsService,
              private actions$: Actions) {
  }
}
