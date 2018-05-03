package tv.v1x1.common.services.slack.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChannelRepliesResponse extends Response {
    @JsonProperty
    private List<MessageEvent> messages;
    @JsonProperty("has_more")
    private boolean hasMore;

    public ChannelRepliesResponse() {
        super(true);
    }

    public ChannelRepliesResponse(final List<MessageEvent> messages, final boolean hasMore) {
        super(true);
        this.messages = messages;
        this.hasMore = hasMore;
    }

    public List<MessageEvent> getMessages() {
        return messages;
    }

    public void setMessages(final List<MessageEvent> messages) {
        this.messages = messages;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(final boolean hasMore) {
        this.hasMore = hasMore;
    }
}
