import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubFrame} from "./v1x1_pub_sub_frame";
@JsonObject
export class V1x1PubSubPublishRequestFrame extends V1x1PubSubFrame {
  @JsonProperty("topic")
  topic: string = undefined;
  @JsonProperty("payload")
  payload: string = undefined;

  constructor(topic?: string, payload?: string) {
    super(topic && "LISTEN_REQUEST");
    if(topic)
      this.topic = topic;
    if(payload)
      this.payload = payload;
  }
}
