import {V1x1GlobalUser} from "../../model/api/v1x1_global_user";
import {Component, Input} from "@angular/core";

@Component({
  selector: 'user-formatter',
  template: `
    <mat-list *ngIf="!matMenuItem">
      <mat-list-item *ngFor="let user of globalUser.users; last as last">
        <platform-formatter [platform]="user.platform">{{user.displayName}}</platform-formatter>
      </mat-list-item>
    </mat-list>
    <span mat-menu-item [disabled]="true" *ngFor="let user of matMenuItem ? globalUser.users : []; let idx = index">
      <platform-formatter [platform]="user.platform">{{user.displayName}}</platform-formatter>
    </span>
  `
})
export class UserFormatterComponent {
  @Input() globalUser: V1x1GlobalUser;
  @Input() matMenuItem: boolean = false;
}
