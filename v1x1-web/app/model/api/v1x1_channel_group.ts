import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1Channel} from "./v1x1_channel";

@JsonObject
export class V1x1ChannelGroup {
  @JsonProperty("platform")
  platform: string = undefined;
  @JsonProperty("id")
  id: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
  @JsonProperty("channels", [V1x1Channel])
  channels: V1x1Channel[] = undefined;
}
