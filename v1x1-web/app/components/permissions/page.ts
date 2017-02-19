import {Component, Input} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
@Component({
  selector: 'permissions-page',
  template: `
    <nav class="navbar navbar-toggleable-md navbar-light bg-faded subnav">
      <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item" [class.active]="activeIdx === 0">
            <a class="nav-link" href="#" (click)="setActive(0);">Groups</a>
          </li>
          <li class="nav-item" [class.active]="activeIdx === 1" *ngIf="false">
            <a class="nav-link" href="#" (click)="setActive(1);">Mapping</a>
          </li>
          <li class="nav-item" [class.active]="activeIdx === 2" *ngIf="false">
            <a class="nav-link" href="#" (click)="setActive(2);">Users</a>
          </li>
        </ul>
      </div>
    </nav>
    <permissions-groups-page [activeTenant]="activeTenant" *ngIf="activeIdx === 0"></permissions-groups-page>
  `
})
export class PermissionsPageComponent {
  @Input() activeTenant: V1x1Tenant;
  activeIdx: number = 0;

  setActive(idx: number) {
    this.activeIdx = idx;
  }
}
