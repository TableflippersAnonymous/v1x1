import {V1x1Configuration} from "./v1x1_configuration";
import {V1x1ChannelConfigurationWrapper} from "./v1x1_channel_configuration_wrapper";

export class V1x1ChannelGroupConfiguration {
  channelGroup: V1x1Configuration;
  channels: V1x1ChannelConfigurationWrapper[];

  constructor(channelGroup: V1x1Configuration, channels: V1x1ChannelConfigurationWrapper[]) {
    this.channelGroup = channelGroup;
    this.channels = channels;
  }

  dirty(): boolean {
    return this.channelGroup.dirty() || this.childrenDirty();
  }

  childrenDirty(): boolean {
    for(let i = 0; i < this.channels.length; i++)
      if(this.channels[i].config.dirty())
        return true;
    return false;
  }
}
