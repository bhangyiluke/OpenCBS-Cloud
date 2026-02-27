import { Component } from '@angular/core';

@Component({
  standalone: false,
  selector: 'cbs-user-dropdown',
  templateUrl: 'user-dropdown.component.html',
  styleUrls: ['user-dropdown.component.scss']
})
export class UserDropdownComponent {
  public open = false;
}
