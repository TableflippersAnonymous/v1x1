import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1ChannelGroupPlatformGroup {
  @JsonProperty("name")
  name: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
}
