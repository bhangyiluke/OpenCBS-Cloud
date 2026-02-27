import { Action } from '@ngrx/store';

export class NgRxAction implements Action {
  readonly type: string;
  payload?: any;
}
