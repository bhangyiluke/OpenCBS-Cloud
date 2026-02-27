import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import {Actions, createEffect, ofType} from '@ngrx/effects';


import { ProfilesListService } from './profiles-list.service';
import * as profileListActions from './profiles-list.actions';
import { NgRxAction } from '../../action.interface';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class ProfilesListEffects {

  get_profiles$ = createEffect(() => this.actions$
    .pipe(ofType(profileListActions.LOAD_PROFILES),
      switchMap((action: NgRxAction) => {
        return this.profilesListService.getProfiles(action.payload).pipe(
          map(res => new profileListActions.LoadProfilesSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new profileListActions.LoadProfilesFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(private profilesListService: ProfilesListService,
              private actions$: Actions) {
  }
}
