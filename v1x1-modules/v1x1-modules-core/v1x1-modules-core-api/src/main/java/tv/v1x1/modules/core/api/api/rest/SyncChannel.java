package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class SyncChannel {
    @JsonProperty("channel_group_id")
    private String channelGroupId;
    @JsonProperty
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("module_configuration")
    private Map<String, Configuration> moduleConfiguration;

    public SyncChannel() {
    }

    public SyncChannel(final String channelGroupId, final String id, final String displayName,
                       final Map<String, Configuration> moduleConfiguration) {
        this.channelGroupId = channelGroupId;
        this.id = id;
        this.displayName = displayName;
        this.moduleConfiguration = moduleConfiguration;
    }

    public String getChannelGroupId() {
        return channelGroupId;
    }

    public void setChannelGroupId(final String channelGroupId) {
        this.channelGroupId = channelGroupId;
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

    public Map<String, Configuration> getModuleConfiguration() {
        return moduleConfiguration;
    }

    public void setModuleConfiguration(final Map<String, Configuration> moduleConfiguration) {
        this.moduleConfiguration = moduleConfiguration;
    }
}
