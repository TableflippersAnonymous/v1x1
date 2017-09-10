import {V1x1Configuration} from "./v1x1_configuration";
import {V1x1Channel} from "./v1x1_channel";

export class V1x1ChannelConfigurationWrapper {
  channel: V1x1Channel;
  config: V1x1Configuration;

  constructor(channel: V1x1Channel, config: V1x1Configuration) {
    this.channel = channel;
    this.config = config;
  }
}
