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

export class V1x1ConfigurationDefinitionField {
  displayName: string;
  description: string;
  defaultValue: string;
  configType: ConfigType;
  requires: string[];
  tenantPermission: Permission;
  jsonField: string;
  complexType: string;

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
