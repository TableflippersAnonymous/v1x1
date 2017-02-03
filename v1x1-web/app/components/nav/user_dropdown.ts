import {Component} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1GlobalUser} from "../../model/v1x1_global_user";
@Component({
  selector: 'user-dropdown-nav-component',
  template: `
    <li class="nav-item" ngbDropdown>
      <a class="nav-link" href="#" id="navbarUserDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle>
        User
      </a>
      <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarUserDropdownMenuLink" *ngIf="globalUser !== null">
        <span class="dropdown-item" *ngFor="let user of globalUser.users"><span class="fa" [class.fa-twitch]="user.platform === 'TWITCH'">/</span>{{user.displayName}}</span>
        <hr>
        <a class="dropdown-item" href="#">Link/Unlink Users</a>
        <a class="dropdown-item" href="#">Switch Users</a>
      </div>
    </li>
  `
})
export class UserDropdownNavComponent {
  globalUser: V1x1GlobalUser = null;

  constructor(private api: V1x1Api) {
    this.api.getSelf().subscribe(r => this.globalUser = r);
  }
}
