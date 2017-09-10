import {Component} from "@angular/core";
import {V1x1Api} from "../../services/api";
import {V1x1ApiCache} from "../../services/api_cache";
import {V1x1GlobalState} from "../../services/global_state";
import {V1x1ChannelGroupPlatformMappingWrapper} from "../../model/v1x1_channel_group_platform_mapping_wrapper";

@Component({
  selector: 'permissions-mapping-page',
  template: `
    <ngb-tabset class="tabs-left">
      <ngb-tab *ngFor="let channelGroupPlatformMapping of channelGroupPlatformMappings">
        <template ngbTabTitle>
          <span [class.color-twitch]="channelGroupPlatformMapping.channelGroup.platform === 'TWITCH'" [class.color-discord]="channelGroupPlatformMapping.channelGroup.platform === 'DISCORD'">
            <i [class.fa-twitch]="channelGroupPlatformMapping.channelGroup.platform === 'TWITCH'" [class.fa-discord]="channelGroupPlatformMapping.channelGroup.platform === 'DISCORD'" [class.fab]="true"></i> {{channelGroupPlatformMapping.channelGroup.displayName}}
          </span>
        </template>
        <template ngbTabContent>
          <permissions-group-mapping-page [channelGroupPlatformMapping]="channelGroupPlatformMapping"></permissions-group-mapping-page>
        </template>
      </ngb-tab>
    </ngb-tabset>
  `
})
export class PermissionsMappingComponent {
  channelGroupPlatformMappings: V1x1ChannelGroupPlatformMappingWrapper[] = [];

  constructor(private cachedApi: V1x1ApiCache, private api: V1x1Api, private globalState: V1x1GlobalState) {
    this.recalculate();
  }

  recalculate() {
    this.globalState.activeTenant.get().subscribe(tenant => {
      this.api.getTenantPlatformMappings(tenant)
        .subscribe(tenantPlatformMapping => this.channelGroupPlatformMappings = tenantPlatformMapping.channelGroups);
    });
  }
}
