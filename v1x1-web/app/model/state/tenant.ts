import {ChannelGroup} from "./channel_group";
import {Configuration} from "./configuration";
import {TenantGroup} from "./tenant_group";
import {V1x1ChatMessage} from "../api/v1x1_chat_message";

export class Tenant {
  id: string;
  displayName: string;
  channelGroups: Map<string, ChannelGroup>;
  moduleConfiguration: Map<string, Configuration>;
  groups: Map<string, TenantGroup>;
  chatMessages: V1x1ChatMessage[];

  constructor(id: string, displayName: string, channelGroups: Map<string, ChannelGroup>,
              moduleConfiguration: Map<string, Configuration>, groups: Map<string, TenantGroup>,
              chatMessages: V1x1ChatMessage[]) {
    this.id = id;
    this.displayName = displayName;
    this.channelGroups = channelGroups;
    this.moduleConfiguration = moduleConfiguration;
    this.groups = groups;
    this.chatMessages = chatMessages;
  }
}
