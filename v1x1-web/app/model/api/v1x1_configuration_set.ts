import {Configuration} from "../state/configuration";
import {V1x1ChannelGroupConfigurationWrapper} from "./v1x1_channel_group_configuration_wrapper";

export class V1x1ConfigurationSet {
  tenant: Configuration;
  channelGroups: V1x1ChannelGroupConfigurationWrapper[];

  constructor(tenant: Configuration, channelGroups: V1x1ChannelGroupConfigurationWrapper[]) {
    this.tenant = tenant;
    this.channelGroups = channelGroups;
  }

  dirty(): boolean {
    return this.tenant.dirty() || this.childrenDirty();
  }

  childrenDirty(): boolean {
    for(let i = 0; i < this.channelGroups.length; i++)
      if(this.channelGroups[i].config.dirty())
        return true;
    return false;
  }

  setOriginal(original: V1x1ConfigurationSet) {
    this.tenant.setOriginal(original.tenant);
    this.channelGroups.forEach(channelGroup => {
      channelGroup.config.setOriginal(original.channelGroups.find(
        originalWrapper => channelGroup.channelGroup.platform === originalWrapper.channelGroup.platform &&
          channelGroup.channelGroup.id === originalWrapper.channelGroup.id
      ));
    });
  }
}
