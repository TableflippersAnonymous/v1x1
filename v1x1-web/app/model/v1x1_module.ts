import {V1x1ConfigurationDefinitionSet} from "./v1x1_configuration_definition_set";
export class V1x1Module {
  name: string;
  configurationDefinitionSet: V1x1ConfigurationDefinitionSet;

  constructor(name: string) {
    this.name = name;
  }
}
