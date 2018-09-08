import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject
export class V1x1Group {
  @JsonProperty("tenant_id")
  tenantId: string = undefined;
  @JsonProperty("group_id")
  groupId: string = undefined;
  @JsonProperty("name")
  name: string = undefined;
  @JsonProperty("permissions", [String])
  permissions: string[] = undefined;
}
