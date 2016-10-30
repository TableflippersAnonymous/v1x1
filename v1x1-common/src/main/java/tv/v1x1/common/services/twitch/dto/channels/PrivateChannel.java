package tv.v1x1.common.services.twitch.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.Channel;

/**
 * Created by cobi on 10/29/2016.
 */
public class PrivateChannel extends Channel {
    @JsonProperty
    private String email;
    @JsonProperty("stream_key")
    private String streamKey;

    public PrivateChannel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(final String streamKey) {
        this.streamKey = streamKey;
    }
}
