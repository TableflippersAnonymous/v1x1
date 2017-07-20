import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubResponseFrame} from "./v1x1_pub_sub_response_frame";
@JsonObject
export class V1x1PubSubAuthResponseFrame extends V1x1PubSubResponseFrame {
  @JsonProperty("principal")
  principal: string = undefined;
}
