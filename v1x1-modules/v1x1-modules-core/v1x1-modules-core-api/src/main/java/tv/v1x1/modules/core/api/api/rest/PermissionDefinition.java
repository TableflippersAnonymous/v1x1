package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PermissionDefinition {
    public static class Entry {
        @JsonProperty
        private String node;
        @JsonProperty("display_name")
        private String displayName;
        @JsonProperty
        private String description;
        @JsonProperty("default_groups")
        private List<String> defaultGroups;

        public Entry() {
        }

        public Entry(final String node, final String displayName, final String description, final List<String> defaultGroups) {
            this.node = node;
            this.displayName = displayName;
            this.description = description;
            this.defaultGroups = defaultGroups;
        }

        public String getNode() {
            return node;
        }

        public void setNode(final String node) {
            this.node = node;
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

        public List<String> getDefaultGroups() {
            return defaultGroups;
        }

        public void setDefaultGroups(final List<String> defaultGroups) {
            this.defaultGroups = defaultGroups;
        }
    }

    @JsonProperty("module_name")
    private String moduleName;
    @JsonProperty
    private int version;
    @JsonProperty
    private List<Entry> entries;

    public PermissionDefinition() {
    }

    public PermissionDefinition(final String moduleName, final int version, final List<Entry> entries) {
        this.moduleName = moduleName;
        this.version = version;
        this.entries = entries;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(final List<Entry> entries) {
        this.entries = entries;
    }
}
