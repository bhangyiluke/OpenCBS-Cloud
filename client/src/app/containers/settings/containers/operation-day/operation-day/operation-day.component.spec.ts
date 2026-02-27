import { waitForAsync as async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';

import { OperationDayComponent } from './operation-day.component';
import { CoreModule } from '../../../core/core.module';
import { RouterTestingModule } from '@angular/router/testing';
import { NglModule } from 'ng-lightning';
import { Observable, of } from 'rxjs';
import { OperationDayService } from '../shared/operation-day.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';

class FakeLoader implements TranslateLoader {
  getTranslation(lang: string): Observable<any> {
    return of({});
  }
}

class MockOperationDayService {
  checkStatus() {
    return of('not_started');
  }

  initStatusCheck() {
    return of([]);
  }

  startEndOfDay() {
    return of(null)
  }
}

describe('OperationDayComponent', () => {
  let component: OperationDayComponent;
  let fixture: ComponentFixture<OperationDayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        CoreModule.forRoot(),
        NglModule,
        ToastrModule.forRoot(),
        TranslateModule.forRoot({
          loader: {provide: TranslateLoader, useClass: FakeLoader}
        })
      ],
      declarations: [OperationDayComponent],
      providers: [
        ToastrService,
        {
          provide: OperationDayService,
          useClass: MockOperationDayService
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OperationDayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
