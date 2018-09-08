import {V1x1ChannelGroup} from "./v1x1_channel_group";
import {V1x1ChannelGroupPlatformMapping} from "./v1x1_channel_group_platform_mapping";

export class V1x1ChannelGroupPlatformMappingWrapper {
  public channelGroup: V1x1ChannelGroup;
  public mappings: V1x1ChannelGroupPlatformMapping[];
  
  constructor(channelGroup: V1x1ChannelGroup, mappings: V1x1ChannelGroupPlatformMapping[]) {
    this.channelGroup = channelGroup;
    this.mappings = mappings;
  }
}
