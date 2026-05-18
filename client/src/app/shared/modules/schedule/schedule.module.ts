import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ScheduleComponent } from './schedule.component';
import { FullCalendarModule } from '@fullcalendar/angular';

@NgModule({
  imports: [CommonModule, FullCalendarModule],
    exports: [ScheduleComponent],
    declarations: [ScheduleComponent]
})
export class ScheduleModule {
}
