package tv.v1x1.common.services.twitch.dto.streams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamList {
    @JsonProperty("_total")
    private long total;
    @JsonProperty
    private List<Stream> streams;

    public StreamList() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(final List<Stream> streams) {
        this.streams = streams;
    }
}
