import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';

import { ChartOfAccountCreateActions } from './chart-of-account-create.actions';
import { ChartOfAccountCreateService } from './chart-of-account-create.service';
import { ReduxBaseEffects } from '../redux-base';

@Injectable()
export class ChartOfAccountCreateEffects {

  create_chart_of_account$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.chartOfAccountCreateActions, (action) => {
    return this.chartOfAccountCreateService.createChartOfAccount(action.payload.data);
  })) as any);

  constructor(private chartOfAccountCreateService: ChartOfAccountCreateService,
              private chartOfAccountCreateActions: ChartOfAccountCreateActions,
              private actions$: Actions) {
  }
}
