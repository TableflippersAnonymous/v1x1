package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class I18nDefinition {
    public static class Entry {
        @JsonProperty
        private String key;
        @JsonProperty
        private String message;
        @JsonProperty("display_name")
        private String displayName;
        @JsonProperty
        private String description;

        public Entry() {
        }

        public Entry(final String key, final String message, final String displayName, final String description) {
            this.key = key;
            this.message = message;
            this.displayName = displayName;
            this.description = description;
        }

        public String getKey() {
            return key;
        }

        public void setKey(final String key) {
            this.key = key;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
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
    }

    @JsonProperty("module_name")
    private String moduleName;
    @JsonProperty
    private int version;
    @JsonProperty
    private List<Entry> entries;

    public I18nDefinition() {
    }

    public I18nDefinition(final String moduleName, final int version, final List<Entry> entries) {
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
