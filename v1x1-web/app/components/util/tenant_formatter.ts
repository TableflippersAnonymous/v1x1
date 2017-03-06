import {Component, Input} from "@angular/core";
import {V1x1Tenant} from "../../model/v1x1_tenant";
@Component({
  selector: 'tenant-formatter',
  template: `
    <span *ngFor="let channel of tenant.channels; let idx = index">
      <span class="fa" [class.fa-twitch]="channel.platform === 'TWITCH'">/</span>{{channel.displayName}}
      <br *ngIf="idx !== tenant.channels.length - 1">
    </span>
  `
})
export class TenantFormatterComponent {
  @Input() tenant: V1x1Tenant;
}
