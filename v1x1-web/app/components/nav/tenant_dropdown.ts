import {Component, EventEmitter, Output} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {Router} from "@angular/router";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {Observable} from "rxjs";

@Component({
  selector: 'tenant-dropdown-nav-component',
  template: `
    <span *ngIf="globalState.activeTenant.getCurrent() !== undefined">
      <button mat-button [matMenuTriggerFor]="appMenu">
        Managing: {{displayName() | async}}
      </button>
      <mat-menu #appMenu="matMenu">
        <button mat-menu-item *ngFor="let tenant of globalState.tenants.get() | async" class="dropdown-item" (click)="setActive(tenant);">
          <tenant-formatter [tenant]="tenant"></tenant-formatter>
        </button>
        <hr>
        <button mat-menu-item>+ Create new</button>
      </mat-menu>
    </span>
  `
})
export class TenantDropdownNavComponent {
  @Output() public activeTenantChange = new EventEmitter();

  constructor(private api: V1x1Api,
              private globalState: V1x1GlobalState,
              private router: Router) { }

  setActive(tenant: V1x1Tenant) {
    this.activeTenantChange.emit(tenant);
    return false;
  }

  displayName(): Observable<string> {
    return this.globalState.activeTenant.get().map(tenant => tenant !== undefined ? tenant.displayName : "???");
  }
}
