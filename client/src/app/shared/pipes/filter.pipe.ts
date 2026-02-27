import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'cbsFilter',
  standalone: false
})
export class FilterPipe implements PipeTransform {
  transform(items: any[], term: string): any {
    return term
      ? items.filter(item => item.name.toLowerCase().indexOf(term) !== -1)
      : items;
  }
}
