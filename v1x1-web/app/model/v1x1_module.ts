import {V1x1ConfigurationDefinitionSet} from "./v1x1_configuration_definition_set";
import {V1x1ConfigurationSet} from "./v1x1_configuration_set";
import {V1x1Configuration} from "./v1x1_configuration";
import {V1x1ConfigurationDefinition} from "./v1x1_configuration_definition";
export class V1x1Module {
  name: string;
  configurationDefinitionSet: V1x1ConfigurationDefinitionSet;
  configurationSet: V1x1ConfigurationSet;

  constructor(name: string, displayName: string, description: string, configurationDefinitionSet: V1x1ConfigurationDefinitionSet) {
    this.name = name;
    this.configurationDefinitionSet = configurationDefinitionSet;
    this.configurationSet = new V1x1ConfigurationSet(new V1x1Configuration({}), new V1x1Configuration({}), new V1x1Configuration({}));
  }

  dirty() {
    return this.configurationSet.global.dirty()
        || this.configurationSet.tenant.dirty()
        || this.configurationSet.channel.dirty();
  }

  get displayName(): string {
    return this.configDef().display_name;
  }

  get description(): string {
    return this.configDef().description;
  }

  private configDef(): V1x1ConfigurationDefinition {
    let configDef: V1x1ConfigurationDefinition = this.configurationDefinitionSet.tenant;
    if(configDef === null)
      configDef = this.configurationDefinitionSet.channel;
    if(configDef === null)
      configDef = this.configurationDefinitionSet.global;
    return configDef;
  }
}
