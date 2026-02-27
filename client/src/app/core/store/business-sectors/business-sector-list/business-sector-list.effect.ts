import { Injectable } from '@angular/core';
import { BusinessSectorListActions } from './business-sector-list.actions';
import { Actions, createEffect } from '@ngrx/effects';
import { BusinessSectorListService } from './business-sector-list.service';
import { ReduxBaseEffects } from '../../redux-base/redux.base.effects';
import { Observable, of } from 'rxjs';

@Injectable()
export class BusinessSectorListEffects {
  load_business_sectors$= (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.businessSectorListActions, () => {
    return this.businessSectorListService.getBusinessSectorList();
  })) as any);

  constructor(private businessSectorListService: BusinessSectorListService,
              private businessSectorListActions: BusinessSectorListActions,
              private actions$: Actions) {
  }
}
