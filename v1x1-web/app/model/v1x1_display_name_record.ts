import {JsonObject, JsonProperty} from "json2typescript";
@JsonObject
export class V1x1DisplayNameRecord {
  @JsonProperty("platform")
  platform: string = undefined;
  @JsonProperty("user_id")
  userId: string = undefined;
  @JsonProperty("username")
  username: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
  @JsonProperty("global_user_id")
  globalUserId: string = undefined;
}
