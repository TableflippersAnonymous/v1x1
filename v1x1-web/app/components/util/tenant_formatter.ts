import {Component, Input} from "@angular/core";
import {Tenant} from "../../model/state/tenant";

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
  @Input() tenant: Tenant;
}
