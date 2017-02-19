import {V1x1ConfigurationDefinitionField, Permission} from "./v1x1_configuration_definition_field";
import {JsonObject, JsonProperty, JsonConvert} from "json2typescript";
@JsonObject
export class V1x1ConfigurationDefinition {
  name: string = undefined;
  display_name: string = undefined;
  description: string = undefined;
  @JsonProperty("tenant_permission")
  tenantPermissionString: string = undefined;
  @JsonProperty("fields", [V1x1ConfigurationDefinitionField])
  fields: V1x1ConfigurationDefinitionField[] = [];
  @JsonProperty("complex_fields")
  complexFieldsJson: {[key: string]: any[]} = {};
  complexFieldsObj: {[key: string]: V1x1ConfigurationDefinitionField[]} = undefined;

  get tenantPermission(): Permission {
    return Permission[this.tenantPermissionString];
  }

  get complexFields(): {[key: string]: V1x1ConfigurationDefinitionField[]} {
    if(this.complexFieldsObj === undefined)
      this.calculateComplexFields();
    return this.complexFieldsObj;
  }

  private calculateComplexFields() {
    let obj: {[key: string]: V1x1ConfigurationDefinitionField[]} = {};
    Object.keys(this.complexFieldsJson).forEach(type =>
      obj[type] = this.complexFieldsJson[type].map(
        field => JsonConvert.deserializeObject(field, V1x1ConfigurationDefinitionField)
      )
    );
    this.complexFieldsObj = obj;
  }


}
