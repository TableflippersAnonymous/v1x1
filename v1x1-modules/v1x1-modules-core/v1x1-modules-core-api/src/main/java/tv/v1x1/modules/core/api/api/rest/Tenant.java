package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by naomi on 4/9/2018.
 */
public class Tenant {
    @JsonProperty("id")
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("channel_groups")
    private List<ChannelGroup> channelGroups;

    public Tenant() {
    }

    public Tenant(final String id, final String displayName, final List<ChannelGroup> channelGroups) {
        this.id = id;
        this.displayName = displayName;
        this.channelGroups = channelGroups;
    }

    public Tenant(final tv.v1x1.common.dto.core.Tenant tenant) {
        this(tenant.getId().getValue().toString(), tenant.getDisplayName(),
                tenant.getChannelGroups().stream().map(ChannelGroup::new).collect(Collectors.toList()));
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

    public List<ChannelGroup> getChannelGroups() {
        return channelGroups;
    }

    public void setChannelGroups(final List<ChannelGroup> channelGroups) {
        this.channelGroups = channelGroups;
    }
}
