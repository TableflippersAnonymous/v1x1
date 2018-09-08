import {ChannelGroup} from "./channel_group";
import {Configuration} from "./configuration";

export class Channel {
  channelGroup: ChannelGroup;
  id: string;
  displayName: string;
  moduleConfiguration: Map<string, Configuration>;

  constructor(channelGroup: ChannelGroup, id: string, displayName: string, moduleConfiguration: Map<string, Configuration>) {
    this.channelGroup = channelGroup;
    this.id = id;
    this.displayName = displayName;
    this.moduleConfiguration = moduleConfiguration;
  }
}
