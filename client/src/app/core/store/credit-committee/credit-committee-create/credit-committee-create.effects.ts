import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';


import { CCRuleCreateService } from './credit-committee-create.service';
import * as ccRuleCreateActions from './credit-committee-create.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class CCRuleCreateEffects {

  create_cc_rule$ = createEffect(() => this.actions$
    .pipe(ofType(ccRuleCreateActions.CREATE_CC_RULE),
      switchMap((action: NgRxAction) => {
        return this.ccRuleCreateService.createCCRule(action.payload).pipe(
          map(
            res => new ccRuleCreateActions.CreateCCRuleSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new ccRuleCreateActions.CreateCCRuleFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(private ccRuleCreateService: CCRuleCreateService,
              private actions$: Actions) {
  }
}
