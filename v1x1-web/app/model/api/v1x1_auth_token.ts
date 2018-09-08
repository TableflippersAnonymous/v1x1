import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1AuthToken {
  @JsonProperty("authorization")
  authorization: string = undefined;
  @JsonProperty("expires")
  expires: number = undefined;
}
