import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import { UserUpdateService } from './user-update.service';
import * as userUpdateActions from './user-update.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class UserUpdateEffects {

  update_user$ = createEffect(() => this.actions$
    .pipe(ofType(userUpdateActions.UPDATE_USER),
      switchMap((action: NgRxAction) => {
        return this.userUpdateService.updateUser(action.payload.value, action.payload.userId)
          .pipe(map(() => new userUpdateActions.UpdateUserSuccess()),
            catchError((err: HttpErrorResponse) => {
              const errObj = new userUpdateActions.UpdateUserFailure(err.error);
              return observableOf(errObj);
            }));
      })));


  update_user_password$ = createEffect(() => this.actions$
    .pipe(ofType(userUpdateActions.UPDATE_USER_PASSWORD),
      switchMap((action: NgRxAction) => {
        return this.userUpdateService.updatePassword(action.payload)
          .pipe(map(() => new userUpdateActions.UpdateUserSuccess()),
            catchError((err: HttpErrorResponse) => {
              const errObj = new userUpdateActions.UpdateUserFailure(err.error);
              return observableOf(errObj);
            }));
      })));

  constructor(
    private userUpdateService: UserUpdateService,
    private actions$: Actions) {
  }
}
