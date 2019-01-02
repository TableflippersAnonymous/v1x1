import {JsonObject, JsonProperty} from "json2typescript";

export enum ConfigType {
  INTEGER,
  STRING,
  CREDENTIAL,
  OAUTH,
  MASTER_ENABLE,
  BOOLEAN,
  BOT_NAME,
  STRING_LIST,
  STRING_MAP,
  COMPLEX,
  COMPLEX_LIST,
  COMPLEX_STRING_MAP,
  USER_LIST,
  PERMISSION,
  PERMISSION_LIST,
  GROUP,
  PLATFORM_GROUP,
  FILE
}

export enum Permission {
  READ_WRITE, READ_ONLY, WRITE_ONLY, NONE
}

@JsonObject
export class V1x1ConfigurationDefinitionField {
  @JsonProperty("display_name")
  displayName: string = undefined;
  description: string = undefined;
  @JsonProperty("default_value")
  defaultValue: string = undefined;
  @JsonProperty("config_type")
  configTypeString: string = undefined;
  requires: string[] = undefined;
  @JsonProperty("tenant_permission")
  tenantPermissionString: string = undefined;
  @JsonProperty("json_field")
  jsonField: string = undefined;
  @JsonProperty("complex_type")
  complexType: string = undefined;

  set configType(configType: ConfigType) {
    this.configTypeString = ConfigType[configType];
  }

  get configType(): ConfigType {
    return ConfigType[this.configTypeString];
  }

  get tenantPermission(): Permission {
    return Permission[this.tenantPermissionString];
  }
}
