import {Component} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1ChannelGroupPlatformMappingWrapper} from "../../model/v1x1_channel_group_platform_mapping_wrapper";

@Component({
  selector: 'permissions-mapping-page',
  template: `
    <mat-tab-group>
      <mat-tab *ngFor="let channelGroupPlatformMapping of channelGroupPlatformMappings">
        <ng-template mat-tab-label>
          <platform-formatter [platform]="channelGroupPlatformMapping.channelGroup.platform">{{channelGroupPlatformMapping.channelGroup.displayName}}</platform-formatter>
        </ng-template>
        <permissions-group-mapping-page [channelGroupPlatformMapping]="channelGroupPlatformMapping"></permissions-group-mapping-page>
      </mat-tab>
    </mat-tab-group>
  `
})
export class PermissionsMappingComponent {
  channelGroupPlatformMappings: V1x1ChannelGroupPlatformMappingWrapper[] = [];

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState) {
    this.recalculate();
  }

  recalculate() {
    this.globalState.activeTenant.get().subscribe(tenant => {
      if(tenant === undefined)
        return;
      this.api.getTenantPlatformMappings(tenant)
        .subscribe(tenantPlatformMapping => this.channelGroupPlatformMappings = tenantPlatformMapping.channelGroups);
    });
  }
}
