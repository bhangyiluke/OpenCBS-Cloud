import { of as observableOf } from 'rxjs';

import { catchError, map, switchMap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';

import { PaymentMethodCreateService } from './payment-method-create.service';
import * as paymentMethodCreateActions from './payment-method-create.actions';
import { HttpErrorResponse } from '@angular/common/http';


@Injectable()
export class PaymentMethodCreateEffects {

  create_payment_method$ = createEffect(() => this.actions$
    .pipe(ofType(paymentMethodCreateActions.CREATE_PAYMENT_METHOD),
      switchMap((action: paymentMethodCreateActions.PaymentMethodCreateActions) => {
        return this.paymentMethodCreateService.createPaymentMethod(action.payload).pipe(
          map(
            res => new paymentMethodCreateActions.CreatePaymentMethodSuccess(res)),
          catchError((err: HttpErrorResponse) => {
            const errObj = new paymentMethodCreateActions.CreatePaymentMethodFailure(err.error);
            return observableOf(errObj);
          }));
      })));

  constructor(
    private paymentMethodCreateService: PaymentMethodCreateService,
    private actions$: Actions) {
  }
}
