import { Component, OnInit } from '@angular/core';

@Component({
  standalone: false,
  selector: 'cbs-configuration-wrap',
  template: `<router-outlet></router-outlet>`,
})
export class ConfigurationWrapComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

}
