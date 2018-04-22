import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1ConfigChange {
  @JsonProperty("module")
  module: string = undefined;
}
