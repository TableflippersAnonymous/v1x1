package tv.v1x1.common.services.twitch.dto.streams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamSummary {
    @JsonProperty
    private long viewers;
    @JsonProperty
    private long channels;

    public StreamSummary() {
    }

    public long getViewers() {
        return viewers;
    }

    public void setViewers(final long viewers) {
        this.viewers = viewers;
    }

    public long getChannels() {
        return channels;
    }

    public void setChannels(final long channels) {
        this.channels = channels;
    }
}
