import {JsonObject, JsonProperty} from "json2typescript";
import {V1x1PermissionDefinitionEntry} from "./v1x1_permission_definition_entry";

@JsonObject
export class V1x1PermissionDefinition {
  @JsonProperty("module_name")
  moduleName: string = undefined;
  @JsonProperty("version")
  version: number = undefined;
  @JsonProperty("entries", [V1x1PermissionDefinitionEntry])
  entries: V1x1PermissionDefinitionEntry[] = undefined;
}
