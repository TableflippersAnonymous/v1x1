import {Component, OnInit} from "@angular/core";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1ChannelGroupPlatformMappingWrapper} from "../../model/api/v1x1_channel_group_platform_mapping_wrapper";

@Component({
  selector: 'permissions-mapping-page',
  template: `
    <mat-tab-group>
      <mat-tab *ngFor="let channelGroupPlatformMapping of channelGroupPlatformMappings">
        <ng-template mat-tab-label>
          <platform-formatter [platform]="channelGroupPlatformMapping.channelGroup.platform">{{channelGroupPlatformMapping.channelGroup.displayName}}</platform-formatter>
        </ng-template>
        <permissions-group-mapping-page [channelGroupPlatformMapping]="channelGroupPlatformMapping" [activeTenant]="globalState.activeTenant.get() | async"></permissions-group-mapping-page>
      </mat-tab>
    </mat-tab-group>
  `
})
export class PermissionsMappingComponent implements OnInit {
  channelGroupPlatformMappings: V1x1ChannelGroupPlatformMappingWrapper[] = [];

  constructor(private globalState: V1x1GlobalState) {}

  ngOnInit() {
    this.globalState.webapp.currentTenant.subscribe(tenant => {
      if(tenant === undefined)
        return;
      this.api.getTenantPlatformMappings(tenant)
        .subscribe(tenantPlatformMapping => this.channelGroupPlatformMappings = tenantPlatformMapping.channelGroups);
    });
  }
}
