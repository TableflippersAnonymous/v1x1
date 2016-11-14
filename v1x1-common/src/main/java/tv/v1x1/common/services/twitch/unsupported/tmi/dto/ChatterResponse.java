package tv.v1x1.common.services.twitch.unsupported.tmi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 11/13/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatterResponse {
    @JsonProperty("chatter_count")
    private long chatterCount;
    @JsonProperty
    private Chatters chatters;

    public ChatterResponse() {
    }

    public long getChatterCount() {
        return chatterCount;
    }

    public void setChatterCount(final long chatterCount) {
        this.chatterCount = chatterCount;
    }

    public Chatters getChatters() {
        return chatters;
    }

    public void setChatters(final Chatters chatters) {
        this.chatters = chatters;
    }
}
