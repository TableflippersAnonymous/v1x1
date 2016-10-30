package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
public class ChannelList {
    @JsonProperty
    private List<Channel> channels;
    @JsonProperty("_total")
    private long total;

    public ChannelList() {
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(final List<Channel> channels) {
        this.channels = channels;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }
}
