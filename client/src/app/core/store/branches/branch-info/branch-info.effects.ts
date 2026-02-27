import { Injectable } from '@angular/core';
import { BranchInfoActions } from './branch-info.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { BranchInfoService } from './branch-info.service';
import { ReduxBaseEffects } from '../../redux-base/redux.base.effects';
import { Observable, of } from 'rxjs';

@Injectable()
export class BranchInfoEffects {
  load_branch$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.branchInfoActions, (action) => {
    return this.branchInfoService.getBranchInfo(action.payload.data);
  })) as any);

  constructor(private branchInfoService: BranchInfoService,
              private branchInfoActions: BranchInfoActions,
              private actions$: Actions) {
  }
}
