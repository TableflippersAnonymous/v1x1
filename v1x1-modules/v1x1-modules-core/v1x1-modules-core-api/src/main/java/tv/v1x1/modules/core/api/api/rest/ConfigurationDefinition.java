package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Permission;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by naomi on 10/26/2016.
 */
public class ConfigurationDefinition {
    @JsonProperty
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private String description;
    @JsonProperty
    private int version;
    @JsonProperty("tenant_permission")
    private Permission tenantPermission;
    @JsonProperty
    private List<Field> fields;
    @JsonProperty("complex_fields")
    private Map<String, List<Field>> complexFields;

    public static ConfigurationDefinition fromCore(final tv.v1x1.common.dto.core.ConfigurationDefinition core) {
        return new ConfigurationDefinition(
                core.getName(),
                core.getDisplayName(),
                core.getDescription(),
                core.getVersion(),
                core.getTenantPermission(),
                core.getFields().stream().map(Field::fromCore).collect(Collectors.toList()),
                core.getComplexFields().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(Field::fromCore).collect(Collectors.toList())
                ))
        );
    }

    public static class Field {
        @JsonProperty("display_name")
        private String displayName;
        @JsonProperty
        private String description;
        @JsonProperty("default_value")
        private String defaultValue;
        @JsonProperty("config_type")
        private ConfigType configType;
        @JsonProperty
        private List<String> requires;
        @JsonProperty("tenant_permission")
        private Permission tenantPermission;
        @JsonProperty("json_field")
        private String jsonField;
        @JsonProperty("complex_type")
        private String complexType;

        public static Field fromCore(final tv.v1x1.common.dto.core.ConfigurationDefinition.Field field) {
            return new Field(
                    field.getDisplayName(),
                    field.getDescription(),
                    field.getDefaultValue(),
                    field.getConfigType(),
                    field.getRequires(),
                    field.getTenantPermission(),
                    field.getJsonField(),
                    field.getComplexType()
            );
        }

        public Field() {
        }

        public Field(final String displayName, final String description, final String defaultValue, final ConfigType configType, final List<String> requires, final Permission tenantPermission, final String jsonField, final String complexType) {
            this.displayName = displayName;
            this.description = description;
            this.defaultValue = defaultValue;
            this.configType = configType;
            this.requires = requires;
            this.tenantPermission = tenantPermission;
            this.jsonField = jsonField;
            this.complexType = complexType;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(final String displayName) {
            this.displayName = displayName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(final String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public ConfigType getConfigType() {
            return configType;
        }

        public void setConfigType(final ConfigType configType) {
            this.configType = configType;
        }

        public List<String> getRequires() {
            return requires;
        }

        public void setRequires(final List<String> requires) {
            this.requires = requires;
        }

        public Permission getTenantPermission() {
            return tenantPermission;
        }

        public void setTenantPermission(final Permission tenantPermission) {
            this.tenantPermission = tenantPermission;
        }

        public String getJsonField() {
            return jsonField;
        }

        public void setJsonField(final String jsonField) {
            this.jsonField = jsonField;
        }

        public String getComplexType() {
            return complexType;
        }

        public void setComplexType(final String complexType) {
            this.complexType = complexType;
        }
    }

    public ConfigurationDefinition() {
    }

    public ConfigurationDefinition(final String name, final String displayName, final String description, final int version, final Permission tenantPermission, final List<Field> fields, final Map<String, List<Field>> complexFields) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.version = version;
        this.tenantPermission = tenantPermission;
        this.fields = fields;
        this.complexFields = complexFields;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Permission getTenantPermission() {
        return tenantPermission;
    }

    public void setTenantPermission(final Permission tenantPermission) {
        this.tenantPermission = tenantPermission;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(final List<Field> fields) {
        this.fields = fields;
    }

    public Map<String, List<Field>> getComplexFields() {
        return complexFields;
    }

    public void setComplexFields(final Map<String, List<Field>> complexFields) {
        this.complexFields = complexFields;
    }
}
