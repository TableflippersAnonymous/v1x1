import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {Router} from "@angular/router";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {Observable} from "rxjs";
@Component({
  selector: 'tenant-dropdown-nav-component',
  template: `
    <li class="nav-item" ngbDropdown *ngIf="globalState.activeTenant.getCurrent() !== undefined">
      <a href="#" class="nav-link" id="navbarTenantDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle (click)="false;">
        Managing: {{displayName() | async}}
      </a>
      <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarTenantDropdownMenuLink">
        <a href="#" *ngFor="let tenant of globalState.tenants.get() | async" class="dropdown-item" [class.active]="tenant.id === globalState.activeTenant.getCurrent()?.id" (click)="setActive(tenant);">
          <tenant-formatter [tenant]="tenant"></tenant-formatter>
        </a>
        <hr>
        <a class="dropdown-item">+ Create new</a>
      </div>
    </li>
  `
})
export class TenantDropdownNavComponent {
  @Output() public activeTenantChange = new EventEmitter();

  constructor(private api: V1x1Api,
              private globalState: V1x1GlobalState,
              private router: Router) { }

  setActive(tenant: V1x1Tenant) {
    this.activeTenantChange.emit(tenant);
    //this.router.navigate(['./', { tenant_id: this.tenants[idx].id }], { relativeTo: this.route });
    return false;
  }

  displayName(): Observable<string> {
    return this.globalState.activeTenant.get().map(tenant => tenant.channels[0].displayName);
  }
}
