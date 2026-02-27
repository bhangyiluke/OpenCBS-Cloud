import { Component, OnInit } from '@angular/core';

@Component({
  standalone: false,
  selector: 'cbs-logo-svg',
  templateUrl: 'cbs-logo.component.html',
  styles: [':host { display: inline-block; height: auto; width: 100%;}']
})
export class CbsLogoSvgComponent implements OnInit {
  constructor() {
  }

  ngOnInit() {
  }
}
