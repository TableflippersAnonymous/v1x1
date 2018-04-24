import {V1x1GlobalUser} from "../../model/v1x1_global_user";
import {Component, Input} from "@angular/core";

@Component({
  selector: 'user-formatter',
  template: `
    <span *ngFor="let user of globalUser.users; let idx = index">
      <platform-formatter [platform]="user.platform">{{user.displayName}}</platform-formatter>
      <br *ngIf="idx !== globalUser.users.length - 1">
    </span>
  `
})
export class UserFormatterComponent {
  @Input() globalUser: V1x1GlobalUser;
}
