package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;

import java.util.List;

@Table(name = "i18n_module_definition")
public class I18nDefinition {
    @UDT(name = "i18n_entry")
    public static class I18nEntry {
        private String key;
        private String message;
        @Field(name = "display_name")
        private String displayName;
        private String description;

        public I18nEntry() {
        }

        public I18nEntry(final String key, final String message, final String displayName, final String description) {
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

    @PartitionKey
    private String name;
    private int version;
    @Column(caseSensitive = true, name = "entries")
    private List<I18nEntry> entries;

    public I18nDefinition() {
    }

    public I18nDefinition(final String name, final int version, final List<I18nEntry> entries) {
        this.name = name;
        this.version = version;
        this.entries = entries;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public List<I18nEntry> getEntries() {
        return entries;
    }

    public void setEntries(final List<I18nEntry> entries) {
        this.entries = entries;
    }
}
