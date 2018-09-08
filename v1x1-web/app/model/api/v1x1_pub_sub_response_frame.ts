import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubFrame} from "./v1x1_pub_sub_frame";

@JsonObject
export class V1x1PubSubResponseFrame extends V1x1PubSubFrame {
  @JsonProperty("response_to")
  responseTo: string = undefined;
}
