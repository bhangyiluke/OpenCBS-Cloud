import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sort',
  standalone: false
})

export class SortPipe implements PipeTransform {
  transform(array: any[]): any[] {
    return array.sort((a: any, b: any) => a.name.localeCompare(b.name));
  }
}
