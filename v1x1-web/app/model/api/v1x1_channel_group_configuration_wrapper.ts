import {V1x1ChannelGroupConfiguration} from "./v1x1_channel_group_configuration";
import {V1x1ChannelGroup} from "./v1x1_channel_group";

export class V1x1ChannelGroupConfigurationWrapper {
  channelGroup: V1x1ChannelGroup;
  config: V1x1ChannelGroupConfiguration;

  constructor(channelGroup: V1x1ChannelGroup, config: V1x1ChannelGroupConfiguration) {
    this.channelGroup = channelGroup;
    this.config = config;
  }
}
