import {Component, Input} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1GlobalUser} from "../../model/v1x1_global_user";
import {V1x1GlobalState} from "../../services/global_state";
import {Router} from "@angular/router";

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
        <a class="dropdown-item" href="#" (click)="navigateToUsers();">Link/Unlink Users</a>
        <a class="dropdown-item" href="#" (click)="logout();">Switch Users</a>
      </div>
    </li>
  `
})
export class UserDropdownNavComponent {
  globalUser: V1x1GlobalUser = null;
  @Input() activeTenantUrl: string;

  constructor(private api: V1x1Api, private globalState: V1x1GlobalState, private router: Router) {
    this.api.getSelf().subscribe(r => this.globalUser = r);
  }

  displayName(): string {
    return this.globalUser.users[0].displayName;
  }

  logout() {
    this.globalState.loggedIn.set(false);
    this.router.navigate(["AAAAAAAAAAAAAAAAAAAAAA", "welcome"]);
    localStorage.removeItem("authorization");
    localStorage.removeItem("auth_expiry");
    this.globalState.activeTenant.set(undefined);
    this.globalState.tenants.set([]);
    this.globalState.authorization.set(null);
  }

  navigateToUsers(): boolean {
    this.router.navigate([this.activeTenantUrl, "user"]);
    return false;
  }
}
