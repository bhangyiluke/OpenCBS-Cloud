import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';


import { LoanApplicationFieldsService } from './loan-application-fields.service';
import * as loanApplicationActions from './loan-application-fields.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class LoanApplicationFieldsEffects {

  get_loan_application_fields_meta$ = createEffect(() => this.actions$
    .pipe(ofType(loanApplicationActions.LOAD_LOAN_APPLICATION_FIELDS_META),
      switchMap((action: NgRxAction) => {
        return this.loanApplicationFieldsService.getLoanApplicationFieldsMeta().pipe(
          map(
            res => new loanApplicationActions.LoadLoanApplicationFieldsMetaSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new loanApplicationActions.LoadLoanApplicationFieldsMetaFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(private loanApplicationFieldsService: LoanApplicationFieldsService,
              private actions$: Actions) {
  }
}
