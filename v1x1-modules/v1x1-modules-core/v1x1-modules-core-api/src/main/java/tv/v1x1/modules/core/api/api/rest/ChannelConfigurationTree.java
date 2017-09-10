package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChannelConfigurationTree {
    @JsonProperty
    private Channel channel;
    @JsonProperty("channel_configuration")
    private Configuration channelConfiguration;

    public ChannelConfigurationTree() {
    }

    public ChannelConfigurationTree(final Channel channel, final Configuration channelConfiguration) {
        this.channel = channel;
        this.channelConfiguration = channelConfiguration;
    }

    public Channel getChannel() {
        return channel;
    }

    public Configuration getChannelConfiguration() {
        return channelConfiguration;
    }
}
