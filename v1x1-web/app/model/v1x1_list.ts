import {JsonObject, JsonProperty} from "json2typescript";
@JsonObject
export class V1x1List<T> {
  @JsonProperty("total")
  total: number = undefined;
  @JsonProperty("returned")
  returned: number = undefined;
  @JsonProperty("more")
  more: boolean = undefined;
  @JsonProperty("entries")
  entries: T[] = undefined;
  @JsonProperty("continuation_token")
  continuationToken: string = undefined;
}
