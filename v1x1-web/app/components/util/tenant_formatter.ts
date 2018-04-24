import {Component, Input} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";

@Component({
  selector: 'tenant-formatter',
  template: `
    <span *ngFor="let channelGroup of tenant.channelGroups; let idx = index">
      <platform-formatter [platform]="channelGroup.platform">{{channelGroup.displayName}}</platform-formatter>
      <br *ngIf="idx !== tenant.channelGroups.length - 1">
    </span>
  `
})
export class TenantFormatterComponent {
  @Input() tenant: V1x1Tenant;
}
