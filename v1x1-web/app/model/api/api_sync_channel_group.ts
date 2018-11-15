import {ApiSyncChannel} from "./api_sync_channel";
import {ApiConfiguration} from "./api_configuration";
import {ApiChannelGroupPlatformGroup} from "./api_channel_group_platform_group";

export class ApiSyncChannelGroup {
  platform: string = undefined;
  id: string = undefined;
  channels: {[channelId: string]: ApiSyncChannel} = undefined;
  joined: boolean = undefined;
  tenant_id: string = undefined;
  display_name: string = undefined;
  module_configuration: {[moduleName: string]: ApiConfiguration} = undefined;
  group_mappings: {[platformGroupName: string]: string} = undefined;
  platform_groups: ApiChannelGroupPlatformGroup[] = undefined;
}
