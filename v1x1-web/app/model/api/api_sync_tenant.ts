import {ApiTenantGroup} from "./api_tenant_group";
import {ApiSyncChannelGroup} from "./api_sync_channel_group";
import {ApiConfiguration} from "./api_configuration";

export class ApiSyncTenant {
  id: string = undefined;
  groups: {[groupId: string]: ApiTenantGroup} = undefined;
  display_name: string = undefined;
  channel_groups: {[channelId: string]: ApiSyncChannelGroup} = undefined;
  module_configuration: {[moduleName: string]: ApiConfiguration} = undefined;
}
