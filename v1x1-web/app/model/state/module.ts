import {ConfigurationDefinitionSet} from "./configuration_definition_set";
import {V1x1ConfigurationSet} from "../api/v1x1_configuration_set";
import {V1x1ConfigurationDefinition} from "../api/v1x1_configuration_definition";

export class Module {
  name: string;
  configurationDefinitionSet: ConfigurationDefinitionSet;

  constructor(name: string, configurationDefinitionSet: ConfigurationDefinitionSet) {
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
