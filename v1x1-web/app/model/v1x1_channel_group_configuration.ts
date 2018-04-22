import {V1x1Configuration} from "./v1x1_configuration";
import {V1x1ChannelConfigurationWrapper} from "./v1x1_channel_configuration_wrapper";
import {V1x1ChannelGroupConfigurationWrapper} from "./v1x1_channel_group_configuration_wrapper";

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

  setOriginal(original: V1x1ChannelGroupConfigurationWrapper) {
    if(original === null || original === undefined)
      return;
    this.channelGroup.setOriginal(original.config.channelGroup);
    this.channels.forEach(channel => {
      channel.config.setOriginalWrapper(original.config.channels.find(
        originalWrapper => channel.channel.id === originalWrapper.channel.id
      ));
    });
  }

}
