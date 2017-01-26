import {JsonObject, JsonProperty} from "json2typescript";
export enum ConfigType {
  INTEGER,
  STRING,
  CREDENTIAL,
  TWITCH_OAUTH,
  MASTER_ENABLE,
  BOOLEAN,
  BOT_NAME,
  STRING_LIST,
  STRING_MAP,
  COMPLEX,
  COMPLEX_LIST,
  COMPLEX_STRING_MAP
}

export enum Permission {
  READ_WRITE, READ_ONLY, WRITE_ONLY, NONE
}

var complexConfigTypes = [ConfigType.COMPLEX, ConfigType.COMPLEX_LIST, ConfigType.COMPLEX_STRING_MAP];

@JsonObject
export class V1x1ConfigurationDefinitionField {
  @JsonProperty("display_name")
  displayName: string = undefined;
  description: string = undefined;
  @JsonProperty("default_value")
  defaultValue: string = undefined;
  @JsonProperty("config_type")
  configType: ConfigType = undefined;
  requires: string[] = undefined;
  @JsonProperty("tenant_permission")
  tenantPermission: Permission = undefined;
  @JsonProperty("json_field")
  jsonField: string = undefined;
  @JsonProperty("complex_type")
  complexType: string = undefined;

  constructor(displayName: string, description: string, defaultValue: string, configType: ConfigType, requires: string[], tenantPermission: Permission, jsonField: string, complexType: string) {
    this.displayName = displayName;
    this.description = description;
    this.defaultValue = defaultValue;
    this.configType = configType;
    this.requires = requires;
    this.tenantPermission = tenantPermission;
    this.jsonField = jsonField;
    this.complexType = complexType;
  }
}
