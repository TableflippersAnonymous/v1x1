package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelGroupPlatformGroup {
    @JsonProperty
    private String name;
    @JsonProperty("display_name")
    private String displayName;

    public ChannelGroupPlatformGroup() {
    }

    public ChannelGroupPlatformGroup(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
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
}
