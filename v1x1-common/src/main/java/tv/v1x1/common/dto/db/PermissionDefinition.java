package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.UDT;
import tv.v1x1.common.scanners.permission.DefaultGroup;

import java.util.List;

@Table(name = "permission_module_definition")
public class PermissionDefinition {
    @UDT(name = "permission_entry")
    public static class PermissionEntry {
        private String node;
        @Field(name = "display_name")
        private String displayName;
        private String description;
        @Field(name = "default_groups")
        private List<DefaultGroup> defaultGroups;

        public PermissionEntry() {
        }

        public PermissionEntry(final String node, final String displayName, final String description, final List<DefaultGroup> defaultGroups) {
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

        public List<DefaultGroup> getDefaultGroups() {
            return defaultGroups;
        }

        public void setDefaultGroups(final List<DefaultGroup> defaultGroups) {
            this.defaultGroups = defaultGroups;
        }
    }

    @PartitionKey
    private String name;
    private int version;
    @Column(caseSensitive = true, name = "entries")
    private List<PermissionEntry> entries;

    public PermissionDefinition() {
    }

    public PermissionDefinition(final String name, final int version, final List<PermissionEntry> entries) {
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

    public List<PermissionEntry> getEntries() {
        return entries;
    }

    public void setEntries(final List<PermissionEntry> entries) {
        this.entries = entries;
    }
}
