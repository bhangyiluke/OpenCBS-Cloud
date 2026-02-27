import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import { OperationCreateService } from './operation-create.service';
import * as operationCreateActions from './operation-create.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class OperationCreateEffects {

  create_operation_deposit$ = createEffect(() => this.actions$
    .pipe(ofType(operationCreateActions.CREATE_OPERATION_DEPOSIT),
      switchMap((action: NgRxAction) => {
        return this.operationCreateService.createDeposit(action.payload.tillId, action.payload.value).pipe(
          map(
            res => {
              return new operationCreateActions.CreateOperationSuccess(res);
            }
          ),
          catchError((err: HttpErrorResponse) => {
            const errObj = new operationCreateActions.CreateOperationFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  create_operation_withdraw$ = createEffect(() => this.actions$
    .pipe(ofType(operationCreateActions.CREATE_OPERATION_WITHDRAW),
      switchMap((action: NgRxAction) => {
        return this.operationCreateService.createWithdraw(action.payload.tillId, action.payload.value).pipe(
          map(
            res => {
              return new operationCreateActions.CreateOperationSuccess(res);
            }
          ),
          catchError((err: HttpErrorResponse) => {
            const errObj = new operationCreateActions.CreateOperationFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  create_operation_deposit_saving$ = createEffect(() => this.actions$
    .pipe(ofType(operationCreateActions.CREATE_OPERATION_DEPOSIT_SAVING),
      switchMap((action: NgRxAction) => {
        return this.operationCreateService.createDepositSaving(action.payload.tillId, action.payload.value).pipe(
          map(
            res => {
              return new operationCreateActions.CreateOperationSuccess(res);
            }
          ),
          catchError((err: HttpErrorResponse) => {
            const errObj = new operationCreateActions.CreateOperationFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  create_operation_withdraw_saving$ = createEffect(() => this.actions$
    .pipe(ofType(operationCreateActions.CREATE_OPERATION_WITHDRAW_SAVING),
      switchMap((action: NgRxAction) => {
        return this.operationCreateService.createWithdrawSaving(action.payload.tillId, action.payload.value).pipe(
          map(
            res => {
              return new operationCreateActions.CreateOperationSuccess(res);
            }
          ),
          catchError((err: HttpErrorResponse) => {
            const errObj = new operationCreateActions.CreateOperationFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(
    private operationCreateService: OperationCreateService,
    private actions$: Actions) {
  }
}
