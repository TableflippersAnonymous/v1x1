import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1User} from "./v1x1_user";
import {V1x1Channel} from "./v1x1_channel";

@JsonObject
export class V1x1ChatMessage {
  @JsonProperty("message_id")
  messageId: string = undefined;
  @JsonProperty("originating_module")
  originatingModule: string = undefined;
  @JsonProperty("channel", V1x1Channel)
  channel: V1x1Channel = undefined;
  @JsonProperty("user", V1x1User)
  user: V1x1User = undefined;
  @JsonProperty("text")
  text: string = undefined;
  @JsonProperty("permissions", [String])
  permissions: string[] = undefined;
}
