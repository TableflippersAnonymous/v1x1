package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/2/2017.
 */
public class ChannelPlatformMapping {
    @JsonProperty("platform_group")
    private String platformGroup;
    @JsonProperty("group_id")
    private String groupId;

    public ChannelPlatformMapping() {
    }

    public ChannelPlatformMapping(final String platformGroup, final String groupId) {
        this.platformGroup = platformGroup;
        this.groupId = groupId;
    }

    public String getPlatformGroup() {
        return platformGroup;
    }

    public void setPlatformGroup(final String platformGroup) {
        this.platformGroup = platformGroup;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
}
