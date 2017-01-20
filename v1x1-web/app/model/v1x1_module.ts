import {V1x1ConfigurationDefinitionSet} from "./v1x1_configuration_definition_set";
import {V1x1ConfigurationSet} from "./v1x1_configuration_set";
import {V1x1Configuration} from "./v1x1_configuration";
export class V1x1Module {
  name: string;
  displayName: string;
  description: string;
  configurationDefinitionSet: V1x1ConfigurationDefinitionSet;
  configurationSet: V1x1ConfigurationSet;

  constructor(name: string, displayName: string, description: string, configurationDefinitionSet: V1x1ConfigurationDefinitionSet) {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.configurationDefinitionSet = configurationDefinitionSet;
    this.configurationSet = new V1x1ConfigurationSet(new V1x1Configuration({}), new V1x1Configuration({}), new V1x1Configuration({}));
  }
}
