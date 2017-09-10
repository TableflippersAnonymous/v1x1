import {V1x1ChannelGroup} from "./v1x1_channel_group";
import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1Tenant {
  @JsonProperty("id")
  id: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
  @JsonProperty("channel_groups", [V1x1ChannelGroup])
  channelGroups: V1x1ChannelGroup[] = undefined;
}
