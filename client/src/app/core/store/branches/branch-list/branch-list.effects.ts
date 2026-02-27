import { Injectable } from '@angular/core';
import { BranchListActions } from './branch-list.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { BranchListService } from './branch-list.service';
import { ReduxBaseEffects } from '../../redux-base';
import { Observable, of } from 'rxjs';

@Injectable()
export class BranchListEffects {
  load_branches$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.branchListActions, (action) => {
    return this.branchListService.getBranchList(action.payload.data);
  })) as any);

  constructor(private branchListService: BranchListService,
              private branchListActions: BranchListActions,
              private actions$: Actions) {
  }
}
