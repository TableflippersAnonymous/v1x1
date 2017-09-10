import {V1x1Configuration} from "./v1x1_configuration";
import {V1x1ChannelGroupConfigurationWrapper} from "./v1x1_channel_group_configuration_wrapper";

export class V1x1ConfigurationSet {
  tenant: V1x1Configuration;
  channelGroups: V1x1ChannelGroupConfigurationWrapper[];

  constructor(tenant: V1x1Configuration, channelGroups: V1x1ChannelGroupConfigurationWrapper[]) {
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
}
