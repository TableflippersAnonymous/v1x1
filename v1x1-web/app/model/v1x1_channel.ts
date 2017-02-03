import {JsonObject, JsonProperty} from "json2typescript";
@JsonObject
export class V1x1Channel {
  @JsonProperty("tenant_id")
  tenantId: string = undefined;
  @JsonProperty("platform")
  platform: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
  @JsonProperty("channel_id")
  channelId: string = undefined;
}
