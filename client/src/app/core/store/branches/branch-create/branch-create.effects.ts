import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';

import { BranchCreateActions } from './branch-create.actions';
import { BranchCreateService } from './branch-create.service';
import { ReduxBaseEffects } from '../../redux-base';

@Injectable()
export class BranchCreateEffects {

  create_branch$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.branchCreateActions, (action) => {
    return this.branchCreateService.createBranch(action.payload.data);
  })) as any);

  constructor(private branchCreateService: BranchCreateService,
              private branchCreateActions: BranchCreateActions,
              private actions$: Actions) {
  }
}
