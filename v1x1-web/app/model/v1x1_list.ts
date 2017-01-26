import {JsonObject, JsonProperty} from "json2typescript";
@JsonObject
export class V1x1List<T> {
  total: number = undefined;
  returned: number = undefined;
  more: boolean = undefined;
  entries: T[] = undefined;
  @JsonProperty("continuation_token")
  continuationToken: string = undefined;
}
