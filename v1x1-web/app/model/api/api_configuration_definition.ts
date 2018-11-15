import {ApiConfigurationDefinitionField} from "./api_configuration_definition_field";

export class ApiConfigurationDefinition {
  name: string = undefined;
  description: string = undefined;
  version: number = undefined;
  fields: ApiConfigurationDefinitionField[] = undefined;
  display_name: string = undefined;
  tenant_permission: string = undefined;
  complex_fields: {[key: string]: ApiConfigurationDefinitionField[]} = undefined;
}
