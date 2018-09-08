import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubFrame} from "./v1x1_pub_sub_frame";

@JsonObject
export class V1x1PubSubAuthRequestFrame extends V1x1PubSubFrame {
  @JsonProperty("authorization")
  authorization: string = undefined;

  constructor(authorization?: string) {
    super(authorization && "AUTH_REQUEST");
    if(authorization)
      this.authorization = authorization;
  }
}
