import {V1x1ConfigurationDefinition} from "../api/v1x1_configuration_definition";

export class ConfigurationDefinitionSet {
  global: V1x1ConfigurationDefinition;
  user: V1x1ConfigurationDefinition;

  constructor(global: V1x1ConfigurationDefinition, user: V1x1ConfigurationDefinition) {
    this.global = global;
    this.user = user;
  }
}
