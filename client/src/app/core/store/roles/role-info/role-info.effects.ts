import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import { RoleService } from './role-info.service';
import * as roleActions from './role-info.actions';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable()
export class RoleEffects {

  get_role$ = createEffect(() => this.actions$
    .pipe(ofType(roleActions.LOAD_ROLE),
      switchMap((action: roleActions.RoleActions) => {
        return this.roleService.getRole(action.payload).pipe(
          map(res => new roleActions.LoadRoleSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new roleActions.LoadRoleFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(
    private roleService: RoleService,
    private actions$: Actions) {
  }
}
