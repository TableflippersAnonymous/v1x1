import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1State {
  @JsonProperty("state")
  state: string = undefined;
  @JsonProperty("ttl")
  ttl: number = undefined;
}
