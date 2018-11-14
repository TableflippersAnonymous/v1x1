package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class SyncChannelGroup {
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty
    private String platform;
    @JsonProperty
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty
    private Map<String, SyncChannel> channels;
    @JsonProperty("module_configuration")
    private Map<String, Configuration> moduleConfiguration;
    @JsonProperty("group_mappings")
    private Map<String, String> groupMappings;
    @JsonProperty("platform_groups")
    private List<ChannelGroupPlatformGroup> platformGroups;
    @JsonProperty
    private boolean joined;

    public SyncChannelGroup() {
    }

    public SyncChannelGroup(final String tenantId, final String platform, final String id, final String displayName,
                            final Map<String, SyncChannel> channels, final Map<String, Configuration> moduleConfiguration,
                            final Map<String, String> groupMappings, final List<ChannelGroupPlatformGroup> platformGroups,
                            final boolean joined) {
        this.tenantId = tenantId;
        this.platform = platform;
        this.id = id;
        this.displayName = displayName;
        this.channels = channels;
        this.moduleConfiguration = moduleConfiguration;
        this.groupMappings = groupMappings;
        this.platformGroups = platformGroups;
        this.joined = joined;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(final String platform) {
        this.platform = platform;
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

    public Map<String, SyncChannel> getChannels() {
        return channels;
    }

    public void setChannels(final Map<String, SyncChannel> channels) {
        this.channels = channels;
    }

    public Map<String, Configuration> getModuleConfiguration() {
        return moduleConfiguration;
    }

    public void setModuleConfiguration(final Map<String, Configuration> moduleConfiguration) {
        this.moduleConfiguration = moduleConfiguration;
    }

    public Map<String, String> getGroupMappings() {
        return groupMappings;
    }

    public void setGroupMappings(final Map<String, String> groupMappings) {
        this.groupMappings = groupMappings;
    }

    public List<ChannelGroupPlatformGroup> getPlatformGroups() {
        return platformGroups;
    }

    public void setPlatformGroups(final List<ChannelGroupPlatformGroup> platformGroups) {
        this.platformGroups = platformGroups;
    }

    public boolean isJoined() {
        return joined;
    }

    public void setJoined(final boolean joined) {
        this.joined = joined;
    }
}
