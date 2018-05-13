import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1PermissionDefinitionEntry {
  @JsonProperty("node")
  node: string = undefined;
  @JsonProperty("description")
  description: string = undefined;
  @JsonProperty("display_name")
  displayName: string = undefined;
  @JsonProperty("default_groups", [String])
  defaultGroups: string[] = undefined;
}
