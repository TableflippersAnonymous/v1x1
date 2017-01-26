import {V1x1ConfigurationDefinitionField, Permission} from "./v1x1_configuration_definition_field";
import {JsonObject, JsonProperty} from "json2typescript";
@JsonObject
export class V1x1ConfigurationDefinition {
  @JsonProperty("tenant_permission")
  tenantPermission: Permission;
  fields: V1x1ConfigurationDefinitionField[];
  @JsonProperty("complex_fields")
  complexFields: {[key: string]: V1x1ConfigurationDefinitionField[]};


  constructor(tenantPermission: Permission, fields: V1x1ConfigurationDefinitionField[], complexFields: { [p: string]: V1x1ConfigurationDefinitionField[] }) {
    this.tenantPermission = tenantPermission;
    this.fields = fields;
    this.complexFields = complexFields;
  }
}
