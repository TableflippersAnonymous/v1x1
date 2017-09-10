import {V1x1GlobalUser} from "../../model/v1x1_global_user";
import {Component, Input} from "@angular/core";

@Component({
  selector: 'user-formatter',
  template: `
    <span *ngFor="let user of globalUser.users; let idx = index">
      <i [class.fa-twitch]="user.platform === 'TWITCH'" [class.fa-discord]="user.platform === 'DISCORD'" [class.fab]="true"></i> {{user.displayName}}
      <br *ngIf="idx !== globalUser.users.length - 1">
    </span>
  `
})
export class UserFormatterComponent {
  @Input() globalUser: V1x1GlobalUser;
}
