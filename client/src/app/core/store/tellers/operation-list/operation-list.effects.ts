import { Injectable } from '@angular/core';
import { OperationListActions } from './operation-list.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { OperationListService } from './operation-list.service';
import { ReduxBaseEffects } from '../../redux-base';
import { NgRxAction } from '../../action.interface';

@Injectable()
export class OperationListEffects {
  load_operations$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.operationListActions, (action: NgRxAction) => {
    return this.operationListService
    .getOperationList(action.payload.data.tillId, action.payload.data.currencyId, action.payload.data.params);
  })) as any);

  constructor(private operationListService: OperationListService,
              private operationListActions: OperationListActions,
              private actions$: Actions) {
  }
}
