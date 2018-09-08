import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1User {
  @JsonProperty("global_user_id")
  globalUserId: string = undefined;
  @JsonProperty("platform")
  platform: string = undefined;
  @JsonProperty("user_id")
  userId: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
}
