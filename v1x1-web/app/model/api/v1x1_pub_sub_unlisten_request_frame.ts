import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubFrame} from "./v1x1_pub_sub_frame";

@JsonObject
export class V1x1PubSubUnlistenRequestFrame extends V1x1PubSubFrame {
  @JsonProperty("topic")
  topic: string = undefined;

  constructor(topic?: string) {
    super(topic && "UNLISTEN_REQUEST");
    if(topic)
      this.topic = topic;
  }
}
