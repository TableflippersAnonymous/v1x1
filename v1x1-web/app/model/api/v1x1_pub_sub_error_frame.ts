import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PubSubResponseFrame} from "./v1x1_pub_sub_response_frame";

@JsonObject
export class V1x1PubSubErrorFrame extends V1x1PubSubResponseFrame {
  @JsonProperty("error_type")
  errorType: string = undefined;
  @JsonProperty("error_message")
  errorMessage: string = undefined;
}
