package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by naomi on 4/9/2018.
 */
public class ChannelGroup {
    @JsonProperty("platform")
    private String platform;
    @JsonProperty("id")
    private String id;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("channels")
    private List<Channel> channels;

    public ChannelGroup() {
    }

    public ChannelGroup(final String platform, final String id, final String displayName, final List<Channel> channels) {
        this.platform = platform;
        this.id = id;
        this.displayName = displayName;
        this.channels = channels;
    }

    public ChannelGroup(final tv.v1x1.common.dto.core.ChannelGroup channelGroup) {
        this(channelGroup.getPlatform().name(), channelGroup.getId(), channelGroup.getDisplayName(),
                channelGroup.getChannels().stream().map(Channel::new).collect(Collectors.toList()));
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

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(final List<Channel> channels) {
        this.channels = channels;
    }
}
