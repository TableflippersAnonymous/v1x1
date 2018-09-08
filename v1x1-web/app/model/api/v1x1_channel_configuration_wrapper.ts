import {Configuration} from "../state/configuration";
import {V1x1Channel} from "./v1x1_channel";

export class V1x1ChannelConfigurationWrapper {
  channel: V1x1Channel;
  config: Configuration;

  constructor(channel: V1x1Channel, config: Configuration) {
    this.channel = channel;
    this.config = config;
  }
}
