import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { ReduxBaseEffects } from '../../redux-base';


import { LoanAppCCActivityListActions } from './loan-application-cc-activity-list.actions';
import { LoanAppCCActivityListService } from './loan-application-cc-activity-list.service';
import { Observable, of } from 'rxjs';


@Injectable()
export class LoanAppCCActivityListEffects {

  loan_app_cc_activity_list$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.loanAppCCActivityListActions, (action) => {
    return this.loanAppCCActivityListService.getLoanAppCCActivityList(action.payload.data);
  })) as any);


  constructor(private loanAppCCActivityListService: LoanAppCCActivityListService,
              private loanAppCCActivityListActions: LoanAppCCActivityListActions,
              private actions$: Actions) {
  }
}
