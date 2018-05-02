package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;
import tv.v1x1.common.services.slack.dto.events.Event;

import java.util.List;

public class ChannelHistoryResponse extends Response {
    @JsonProperty
    private String latest;
    @JsonProperty
    private List<Event> messages;
    @JsonProperty("has_more")
    private boolean hasMore;

    public ChannelHistoryResponse() {
        super(true);
    }

    public ChannelHistoryResponse(final String latest, final List<Event> messages, final boolean hasMore) {
        super(true);
        this.latest = latest;
        this.messages = messages;
        this.hasMore = hasMore;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(final String latest) {
        this.latest = latest;
    }

    public List<Event> getMessages() {
        return messages;
    }

    public void setMessages(final List<Event> messages) {
        this.messages = messages;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(final boolean hasMore) {
        this.hasMore = hasMore;
    }
}
