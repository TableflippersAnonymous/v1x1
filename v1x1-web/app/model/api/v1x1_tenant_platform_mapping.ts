import {V1x1ChannelGroupPlatformMappingWrapper} from "./v1x1_channel_group_platform_mapping_wrapper";

export class V1x1TenantPlatformMapping {
  public channelGroups: V1x1ChannelGroupPlatformMappingWrapper[];

  constructor(channelGroups: V1x1ChannelGroupPlatformMappingWrapper[]) {
    this.channelGroups = channelGroups;
  }
}
