import { Pipe, PipeTransform } from '@angular/core';
import moment from 'moment';
import { environment } from '../../../environments/environment';

@Pipe({
  name: 'validateDate',
  standalone: false
})
export class ValidateDatePipe implements PipeTransform {
  private dateFormat: string = '';

  transform(value: string, args?: string): boolean {
    this.dateFormat = args ? args : environment.DATE_FORMAT_MOMENT;
    return moment(value, this.dateFormat, true).isValid();
  }
}
