import { Component } from '@angular/core';

@Component({
  standalone: false,
  selector: 'cbs-dashboard',
  templateUrl: 'dashboard.component.html',
  styleUrls: ['dashboard.component.scss']
})

export class DashboardComponent {
  public svgData = {
    collection: 'standard',
    class: 'apps',
    name: 'apps'
  };
}
