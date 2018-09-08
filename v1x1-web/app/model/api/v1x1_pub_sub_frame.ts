import {JsonObject, JsonProperty} from "json2typescript";

const uuid = require("uuid/v4");
@JsonObject
export class V1x1PubSubFrame {
  @JsonProperty("id")
  id: string = undefined;
  @JsonProperty("type")
  type: string = undefined;

  constructor(type?: string) {
    if(type) {
      this.id = uuid();
      this.type = type;
    }
  }
}
