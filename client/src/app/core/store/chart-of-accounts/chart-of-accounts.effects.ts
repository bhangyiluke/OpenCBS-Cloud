import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';
import { ChartOfAccountsActions } from './chart-of-accounts.actions';
import { ChartOfAccountsService } from './chart-of-accounts.service';
import { ReduxBaseEffects } from '../redux-base';

@Injectable()
export class ChartOfAccountsEffects {


  load_chart_of_accounts$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.accountsActions, () => {
    return this.accountsService.getChartOfAccounts();
  })) as any);

  constructor(private accountsService: ChartOfAccountsService,
              private accountsActions: ChartOfAccountsActions,
              private actions$: Actions) {
  }
}
