package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChannelGroupConfigurationTree {
    @JsonProperty("channel_group_configuration")
    private Configuration channelGroupConfiguration;
    @JsonProperty("channel_group")
    private ChannelGroup channelGroup;
    @JsonProperty("channel_configurations")
    private List<ChannelConfigurationTree> channelConfigurations;

    public ChannelGroupConfigurationTree() {
    }

    public ChannelGroupConfigurationTree(final Configuration channelGroupConfiguration, final ChannelGroup channelGroup,
                                         final List<ChannelConfigurationTree> channelConfigurations) {
        this.channelGroupConfiguration = channelGroupConfiguration;
        this.channelGroup = channelGroup;
        this.channelConfigurations = channelConfigurations;
    }

    public Configuration getChannelGroupConfiguration() {
        return channelGroupConfiguration;
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public List<ChannelConfigurationTree> getChannelConfigurations() {
        return channelConfigurations;
    }
}
