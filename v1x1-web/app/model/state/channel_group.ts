import {Tenant} from "./tenant";
import {Channel} from "./channel";
import {Configuration} from "./configuration";
import {TenantGroup} from "./tenant_group";

export class ChannelGroup {
  tenant: Tenant;
  platform: string;
  id: string;
  displayName: string;
  channels: Map<string, Channel>;
  moduleConfiguration: Map<string, Configuration>;
  groupMappings: Map<string, TenantGroup>;
  joined: boolean;

  constructor(tenant: Tenant, platform: string, id: string, displayName: string, channels: Map<string, Channel>, moduleConfiguration: Map<string, Configuration>, groupMappings: Map<string, TenantGroup>, joined: boolean) {
    this.tenant = tenant;
    this.platform = platform;
    this.id = id;
    this.displayName = displayName;
    this.channels = channels;
    this.moduleConfiguration = moduleConfiguration;
    this.groupMappings = groupMappings;
    this.joined = joined;
  }
}
