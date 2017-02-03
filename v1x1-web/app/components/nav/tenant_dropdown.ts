import {Component, EventEmitter, Input, Output} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
import {V1x1Api} from "../../services/api";
@Component({
  selector: 'tenant-dropdown-nav-component',
  template: `
    <li class="nav-item" ngbDropdown *ngIf="tenants !== null">
      <a class="nav-link" href="#" id="navbarTenantDropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" ngbDropdownToggle>
        Tenant
      </a>
      <div class="dropdown-menu" style="left: auto; right: 0;" aria-labelledby="navbarTenantDropdownMenuLink">
        <a *ngFor="let tenant of tenants; let idx = index" class="dropdown-item" [class.active]="idx === activeIdx" href="#" (click)="setActive(idx);">
          <span *ngFor="let channel of tenant.channels; let cidx = index">
            <span class="fa" [class.fa-twitch]="channel.platform === 'TWITCH'">/</span>{{channel.displayName}}
            <br *ngIf="cidx !== tenant.channels.length - 1">
          </span>
        </a>
        <hr>
        <a class="dropdown-item" href="#">+ Create new</a>
      </div>
    </li>
  `
})
export class TenantDropdownNavComponent {
  tenants: V1x1Tenant[] = null;
  @Input() public activeIdx: number = 0;
  @Output() public activeIdxChange = new EventEmitter();
  @Output() public activeTenantChange = new EventEmitter();


  constructor(private api: V1x1Api) {
    this.api.getTenants().subscribe(r => {
      this.tenants = r;
      this.activeTenantChange.emit(this.tenants[this.activeIdx]);
    });
  }

  setActive(idx: number) {
    this.activeIdx = idx;
    this.activeIdxChange.emit(this.activeIdx);
    this.activeTenantChange.emit(this.tenants[this.activeIdx]);
  }
}
