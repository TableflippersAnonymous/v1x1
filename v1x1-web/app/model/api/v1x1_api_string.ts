import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1ApiString {
  @JsonProperty("value")
  value: string = undefined;
}
