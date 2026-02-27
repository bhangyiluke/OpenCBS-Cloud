import { BorrowingEventsActions } from './borrowing-events.actions';
import { IReduxBase, ReduxBaseReducer } from '../../redux-base/redux.base.reducer';

import { NgRxAction } from '../../action.interface';

export interface IBorrowingEvents extends IReduxBase {
}

export function borrowingEventsReducer(state, {type, payload}: NgRxAction) {
  return ReduxBaseReducer.getConfig(new BorrowingEventsActions(), state, {type, payload})
}


