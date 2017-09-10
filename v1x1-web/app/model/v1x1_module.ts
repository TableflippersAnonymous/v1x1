import {V1x1ConfigurationDefinitionSet} from "./v1x1_configuration_definition_set";
import {V1x1ConfigurationSet} from "./v1x1_configuration_set";
import {V1x1ConfigurationDefinition} from "./v1x1_configuration_definition";

export class V1x1Module {
  name: string;
  configurationDefinitionSet: V1x1ConfigurationDefinitionSet;

  constructor(name: string, configurationDefinitionSet: V1x1ConfigurationDefinitionSet) {
    this.name = name;
    this.configurationDefinitionSet = configurationDefinitionSet;
  }

  dirty(configurationSet: V1x1ConfigurationSet) {
    if(configurationSet === null || configurationSet === undefined)
      return false;
    return configurationSet.dirty();
  }

  get displayName(): string {
    return this.configDef().displayName;
  }

  get description(): string {
    return this.configDef().description;
  }

  private configDef(): V1x1ConfigurationDefinition {
    return this.configurationDefinitionSet.user;
  }
}
