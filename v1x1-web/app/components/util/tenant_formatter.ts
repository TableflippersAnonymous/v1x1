import {Component, Input} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";

@Component({
  selector: 'tenant-formatter',
  template: `
    <span *ngFor="let channelGroup of tenant.channelGroups; let idx = index">
      <i [class.fa-twitch]="channelGroup.platform === 'TWITCH'" [class.fa-discord]="channelGroup.platform === 'DISCORD'" [class.fab]="true"></i> {{channelGroup.displayName}}
      <br *ngIf="idx !== tenant.channelGroups.length - 1">
    </span>
  `
})
export class TenantFormatterComponent {
  @Input() tenant: V1x1Tenant;
}
