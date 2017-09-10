import {V1x1ConfigurationDefinition} from "./v1x1_configuration_definition";

export class V1x1ConfigurationDefinitionSet {
  global: V1x1ConfigurationDefinition;
  user: V1x1ConfigurationDefinition;


  constructor(global: V1x1ConfigurationDefinition, user: V1x1ConfigurationDefinition) {
    this.global = global;
    this.user = user;
  }
}
