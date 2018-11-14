package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SyncTenant {
    @JsonProperty
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("channel_groups")
    private Map<String, SyncChannelGroup> channelGroups;
    @JsonProperty("module_configuration")
    private Map<String, Configuration> moduleConfiguration;
    @JsonProperty
    private Map<String, TenantGroup> groups;

    public SyncTenant() {
    }

    public SyncTenant(final String id, final String displayName, final Map<String, SyncChannelGroup> channelGroups,
                      final Map<String, Configuration> moduleConfiguration, final Map<String, TenantGroup> groups) {
        this.id = id;
        this.displayName = displayName;
        this.channelGroups = channelGroups;
        this.moduleConfiguration = moduleConfiguration;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public Map<String, SyncChannelGroup> getChannelGroups() {
        return channelGroups;
    }

    public void setChannelGroups(final Map<String, SyncChannelGroup> channelGroups) {
        this.channelGroups = channelGroups;
    }

    public Map<String, Configuration> getModuleConfiguration() {
        return moduleConfiguration;
    }

    public void setModuleConfiguration(final Map<String, Configuration> moduleConfiguration) {
        this.moduleConfiguration = moduleConfiguration;
    }

    public Map<String, TenantGroup> getGroups() {
        return groups;
    }

    public void setGroups(final Map<String, TenantGroup> groups) {
        this.groups = groups;
    }
}
