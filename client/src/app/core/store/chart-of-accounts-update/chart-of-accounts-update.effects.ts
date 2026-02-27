import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { ChartOfAccountUpdateActions } from './chart-of-accounts-update.actions';
import { ChartOfAccountUpdateService } from './chart-of-accounts-update.service';
import { ReduxBaseEffects } from '../redux-base';

@Injectable()
export class ChartOfAccountUpdateEffects {

  update_chart_of_account$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.chartOfAccountUpdateActions, (action) => {
    return this.chartOfAccountUpdateService.updateChartOfAccount(action.payload.data.chartOfAccountEditData, action.payload.data.id);
  })) as any);

  constructor(private chartOfAccountUpdateService: ChartOfAccountUpdateService,
              private chartOfAccountUpdateActions: ChartOfAccountUpdateActions,
              private actions$: Actions) {
  }
}
