import {Component} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1GlobalUser} from "../../model/v1x1_global_user";
@Component({
  selector: 'user-dropdown-nav-component',
  template: `
    <li class="nav-item" ngbDropdown *ngIf="globalUser !== null">
      <a href="#" class="nav-link" id="navbarUserDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle (click)="false;">
        Logged in as: {{displayName()}}
      </a>
      <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarUserDropdownMenuLink">
        <div class="dropdown-item">
          <user-formatter [globalUser]="globalUser"></user-formatter>
        </div>
        <hr>
        <a class="dropdown-item">Link/Unlink Users</a>
        <a class="dropdown-item">Switch Users</a>
      </div>
    </li>
  `
})
export class UserDropdownNavComponent {
  globalUser: V1x1GlobalUser = null;

  constructor(private api: V1x1Api) {
    this.api.getSelf().subscribe(r => this.globalUser = r);
  }

  displayName(): string {
    return this.globalUser.users[0].displayName;
  }
}
