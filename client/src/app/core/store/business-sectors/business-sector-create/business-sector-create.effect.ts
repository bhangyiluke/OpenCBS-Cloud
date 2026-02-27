import { Injectable } from '@angular/core';
import { Actions, createEffect } from '@ngrx/effects';

import { BusinessSectorCreateActions } from './business-sector-create.actions';
import { BusinessSectorCreateService } from './business-sector-create.service';
import { ReduxBaseEffects } from '../../redux-base/redux.base.effects';

@Injectable()
export class BusinessSectorCreateEffects {

  create_business_sector$ = (createEffect(() => ReduxBaseEffects.getConfig(this.actions$, this.businessSectorCreateActions, (action) => {
    return this.businessSectorCreateService.createBusinessSector(action.payload.data);
  })) as any);

  constructor(private businessSectorCreateService: BusinessSectorCreateService,
              private businessSectorCreateActions: BusinessSectorCreateActions,
              private actions$: Actions) {
  }
}
