import { Component, OnInit, Input } from '@angular/core';

@Component({
  standalone: false,
  selector: 'cbs-main-nav',
  templateUrl: 'mainnav.component.html',
  styleUrls: ['mainnav.component.scss']
})
export class MainNavComponent implements OnInit {
  @Input() navElements: any[];

  constructor() {
  }

  ngOnInit() {
  }

}
