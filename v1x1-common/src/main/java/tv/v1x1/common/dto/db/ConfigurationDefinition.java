package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.UDT;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Permission;

import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 10/24/2016.
 */
public abstract class ConfigurationDefinition {
    @PartitionKey
    private String name;
    @Column(name = "display_name")
    private String displayName;
    private String description;
    private int version;
    @Column(name = "tenant_permission")
    private Permission tenantPermission;
    private List<Field> fields;
    @Column(name = "complex_fields")
    private Map<String, List<Field>> complexFields;

    @UDT(name = "configuration_definition_field")
    public static class Field {
        @com.datastax.driver.mapping.annotations.Field(name = "display_name")
        private String displayName;
        private String description;
        @com.datastax.driver.mapping.annotations.Field(name = "default_value")
        private String defaultValue;
        @com.datastax.driver.mapping.annotations.Field(name = "config_type")
        private ConfigType configType;
        private List<String> requires;
        @com.datastax.driver.mapping.annotations.Field(name = "tenant_permission")
        private Permission tenantPermission;
        @com.datastax.driver.mapping.annotations.Field(name = "json_field")
        private String jsonField;
        @com.datastax.driver.mapping.annotations.Field(name = "complex_type")
        private String complexType;

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

        public String getDescription() {
            return description;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public ConfigType getConfigType() {
            return configType;
        }

        public List<String> getRequires() {
            return requires == null ? ImmutableList.of() : requires;
        }

        public Permission getTenantPermission() {
            return tenantPermission;
        }

        public String getJsonField() {
            return jsonField;
        }

        public String getComplexType() {
            return complexType;
        }

        public tv.v1x1.common.dto.core.ConfigurationDefinition.Field toCore() {
            return new tv.v1x1.common.dto.core.ConfigurationDefinition.Field(displayName, description, defaultValue, configType, requires, tenantPermission, jsonField, complexType);
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

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getVersion() {
        return version;
    }

    public Permission getTenantPermission() {
        return tenantPermission;
    }

    public List<Field> getFields() {
        return fields == null ? ImmutableList.of() : fields;
    }

    public Map<String, List<Field>> getComplexFields() {
        return complexFields == null ? ImmutableMap.of() : complexFields;
    }

    public abstract tv.v1x1.common.dto.core.ConfigurationDefinition toCore();
}
