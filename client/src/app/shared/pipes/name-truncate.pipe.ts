import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cbsNameTruncate',
  standalone: false
})

export class NameTruncatePipe implements PipeTransform {
  transform(value: string, args: any[]): any {
    if (value) {
      return value.substr(0, 2);
    } else {
      return value;
    }
  }
}
