package tv.v1x1.common.dto.core;

import tv.v1x1.common.config.*;
import tv.v1x1.common.config.Permission;

import java.util.List;

/**
 * Created by cobi on 10/24/2016.
 */
public abstract class ConfigurationDefinition {
    private final String name;
    private final String displayName;
    private final String description;
    private final int version;
    private final tv.v1x1.common.config.Permission tenantPermission;
    private final List<Field> fields;

    public static class Field {
        private final String displayName;
        private final String description;
        private final String defaultValue;
        private final ConfigType configType;
        private final List<String> requires;
        private final tv.v1x1.common.config.Permission tenantPermission;
        private final String jsonField;

        public Field(final String displayName, final String description, final String defaultValue, final ConfigType configType, final List<String> requires, final tv.v1x1.common.config.Permission tenantPermission, final String jsonField) {
            this.displayName = displayName;
            this.description = description;
            this.defaultValue = defaultValue;
            this.configType = configType;
            this.requires = requires;
            this.tenantPermission = tenantPermission;
            this.jsonField = jsonField;
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
            return requires;
        }

        public Permission getTenantPermission() {
            return tenantPermission;
        }

        public String getJsonField() {
            return jsonField;
        }

        public tv.v1x1.common.dto.db.ConfigurationDefinition.Field toDB() {
            return new tv.v1x1.common.dto.db.ConfigurationDefinition.Field(displayName, description, defaultValue, configType, requires, tenantPermission, jsonField);
        }
    }

    public ConfigurationDefinition(final String name, final String displayName, final String description, final int version, final tv.v1x1.common.config.Permission tenantPermission, final List<Field> fields) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.version = version;
        this.tenantPermission = tenantPermission;
        this.fields = fields;
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

    public tv.v1x1.common.config.Permission getTenantPermission() {
        return tenantPermission;
    }

    public List<Field> getFields() {
        return fields;
    }

    public abstract tv.v1x1.common.dto.db.ConfigurationDefinition toDB();
}
