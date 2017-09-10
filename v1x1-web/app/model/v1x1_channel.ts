import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1Channel {
  @JsonProperty("id")
  id: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
}
