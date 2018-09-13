import {Tenant} from "./tenant";
import {Channel} from "./channel";
import {Configuration} from "./configuration";
import {TenantGroup} from "./tenant_group";
import {V1x1ChannelGroupPlatformGroup} from "../api/v1x1_channel_group_platform_group";

export class ChannelGroup {
  tenant: Tenant;
  platform: string;
  id: string;
  displayName: string;
  channels: Map<string, Channel>;
  moduleConfiguration: Map<string, Configuration>;
  groupMappings: Map<string, TenantGroup>;
  platformGroups: V1x1ChannelGroupPlatformGroup[];
  joined: boolean;

  constructor(tenant: Tenant, platform: string, id: string, displayName: string, channels: Map<string, Channel>,
              moduleConfiguration: Map<string, Configuration>, groupMappings: Map<string, TenantGroup>,
              platformGroups: V1x1ChannelGroupPlatformGroup[], joined: boolean) {
    this.tenant = tenant;
    this.platform = platform;
    this.id = id;
    this.displayName = displayName;
    this.channels = channels;
    this.moduleConfiguration = moduleConfiguration;
    this.groupMappings = groupMappings;
    this.platformGroups = platformGroups;
    this.joined = joined;
  }
}
