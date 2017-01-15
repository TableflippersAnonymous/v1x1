import {V1x1ConfigurationDefinitionSet} from "./v1x1_configuration_definition_set";
export class V1x1Module {
  name: string;
  displayName: string;
  description: string;
  configurationDefinitionSet: V1x1ConfigurationDefinitionSet;

  constructor(name: string, displayName: string, description: string, configurationDefinitionSet: V1x1ConfigurationDefinitionSet) {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.configurationDefinitionSet = configurationDefinitionSet;
  }
}
