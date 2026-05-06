import { NgModule } from '@angular/core';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NglModule } from 'ng-lightning';
import { TranslateModule } from '@ngx-translate/core';
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { SharedModule } from 'primeng/api';

import { CbsTreeTableModule } from './modules/cbs-tree-table/treetable.component';
import { FileUploadModule } from './modules/cbs-file-upload/file-upload';
import { CbsCustomFieldBuilderModule } from './modules/cbs-custom-field-builder/cf-builder.module';
import { ChipsModule } from './modules/cbs-chips/chips.module';
import { CbsFormModule } from './modules/cbs-form/cbs-form.module';

import { COMPONENTS } from './COMPONENTS';
import { ScheduleModule } from './modules/schedule/schedule.module';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';

@NgModule({
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    RouterModule,
    TranslateModule,
    SharedModule,
    CbsTreeTableModule,
    FileUploadModule,
    CbsCustomFieldBuilderModule,
    ChipsModule,
    CbsFormModule,
    ScheduleModule,
    NglModule,
    TableModule,
    MultiSelectModule,
    MatMomentDateModule,
    MatDatepickerModule,
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatToolbarModule
  ],
  exports: [
    ...COMPONENTS,
    CbsTreeTableModule,
    FileUploadModule,
    CbsCustomFieldBuilderModule,
    ChipsModule,
    CbsFormModule,
    TableModule,
    ScheduleModule,
    SharedModule,
    MatDatepickerModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatToolbarModule,
    TranslateModule
  ],
  declarations: COMPONENTS
})
export class CbsSharedModule {
}
