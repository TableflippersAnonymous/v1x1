import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubResponseFrame} from "./v1x1_pub_sub_response_frame";

@JsonObject
export class V1x1PubSubPublishResponseFrame extends V1x1PubSubResponseFrame {
  @JsonProperty("from")
  from: string = undefined;
  @JsonProperty("message_id")
  messageId: string = undefined;
  @JsonProperty("timestamp")
  timestamp: number = undefined;
  @JsonProperty("topic")
  topic: string = undefined;
  @JsonProperty("payload")
  payload: string = undefined;
}
