import { map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';


import { LoanAppGuarantorDeleteService } from './loan-app-guarantor-delete.service';
import * as LoanAppGuarantorDeleteActions from './loan-app-guarantor-delete.actions';


@Injectable()
export class LoanAppGuarantorDeleteEffects {

  delete_loan_app_guarantor$ = createEffect(() => this.actions$
    .pipe(ofType(LoanAppGuarantorDeleteActions.DELETE_LOAN_APP_GUARANTOR),
    switchMap((action: LoanAppGuarantorDeleteActions.LoanAppGuarantorDeleteActions) => {
      return this.loanAppGuarantorDeleteService.deleteLoanApplicationGuarantor(
        action.payload.loanAppId,
        action.payload.guarantorId).pipe(map(() => new LoanAppGuarantorDeleteActions.DeleteLoanAppGuarantorSuccess()));
    })));

  constructor(private loanAppGuarantorDeleteService: LoanAppGuarantorDeleteService,
              private actions$: Actions) {
  }
}
