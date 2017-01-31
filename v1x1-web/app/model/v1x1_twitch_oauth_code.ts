import {JsonObject, JsonProperty} from "json2typescript";
@JsonObject
export class V1x1TwitchOauthCode {
  @JsonProperty("oauth_code")
  oauthCode: string = undefined;
  @JsonProperty("oauth_state")
  oauthState: string = undefined;

  constructor(oauthCode: string, oauthState: string) {
    this.oauthCode = oauthCode;
    this.oauthState = oauthState;
  }
}
