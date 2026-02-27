import { Pipe, PipeTransform } from '@angular/core';
import moment from 'moment';
import { SystemSettingsShareService } from '../../core/services';

@Pipe({
  name: 'cbsDateFormat',
  standalone: false
})

export class DateFormatPipe implements PipeTransform {
  private readonly dateFormat: string;

  constructor(private systemSettingsShareService: SystemSettingsShareService) {
    this.dateFormat = this.systemSettingsShareService.getData('DATE_FORMAT');
  }

  transform(value: any) {
    let format;
    if (value) {
      format = moment(value).format(this.dateFormat);
      return format;
    }
    return value;
  }
}
