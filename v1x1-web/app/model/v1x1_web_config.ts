import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1WebConfig {
  @JsonProperty("client_ids")
  clientIds: {[key: string]: string} = undefined;
  @JsonProperty("redirect_uris")
  redirectUris: {[key: string]: string} = undefined;
  @JsonProperty("api_base")
  apiBase: string = undefined;
  @JsonProperty("pubsub_base")
  pubsubBase: string = undefined;
  @JsonProperty("config_oauth_urls")
  configOauthUrls: {[key: string]: string} = undefined;
}
