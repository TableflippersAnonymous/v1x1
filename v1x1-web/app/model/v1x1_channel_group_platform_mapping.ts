import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1ChannelGroupPlatformMapping {
  @JsonProperty("platform_group")
  platformGroup: string = undefined;
  @JsonProperty("group_id")
  groupId: string = undefined;
}
