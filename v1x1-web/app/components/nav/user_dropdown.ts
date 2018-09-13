import {Component, Input} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1GlobalUser} from "../../model/api/v1x1_global_user";
import {V1x1GlobalState} from "../../services/global_state";
import {Router} from "@angular/router";

@Component({
  selector: 'user-dropdown-nav-component',
  template: `
    <span *ngIf="globalUser !== null">
      <button mat-button [matMenuTriggerFor]="appMenu">
        Logged in as: {{displayName()}}
      </button>
      <mat-menu #appMenu="matMenu">
        <user-formatter [globalUser]="globalUser" [matMenuItem]="true"></user-formatter>
        <hr>
        <a mat-menu-item href="#" (click)="navigateToUsers();">Link/Unlink Users</a>
        <a mat-menu-item href="#" (click)="logout();">Switch Users</a>
      </mat-menu>
    </span>
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
    this.globalState.webapp.currentTenant.next(undefined);
    this.globalState.webapp.tenants.next(new Map());
    this.globalState.authorization.set(null);
  }

  navigateToUsers(): boolean {
    this.router.navigate([this.activeTenantUrl, "user"]);
    return false;
  }
}
