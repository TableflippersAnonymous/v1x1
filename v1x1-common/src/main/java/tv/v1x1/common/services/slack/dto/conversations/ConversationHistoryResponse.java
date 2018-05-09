package tv.v1x1.common.services.slack.dto.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;
import tv.v1x1.common.services.slack.dto.misc.ResponseMetadata;

public class ConversationHistoryResponse extends Response {
    @JsonProperty
    private List<MessageEvent> messages;
    @JsonProperty("has_more")
    private Boolean hasMore;
    @JsonProperty("pin_count")
    private Integer pinCount;
    @JsonProperty("response_metadata")
    private ResponseMetadata responseMetadata;

    public ConversationHistoryResponse() {
        super(true);
    }

    public ConversationHistoryResponse(final List<MessageEvent> messages, final Boolean hasMore, final Integer pinCount,
                                       final ResponseMetadata responseMetadata) {
        super(true);
        this.messages = messages;
        this.hasMore = hasMore;
        this.pinCount = pinCount;
        this.responseMetadata = responseMetadata;
    }

    public List<MessageEvent> getMessages() {
        return messages;
    }

    public void setMessages(final List<MessageEvent> messages) {
        this.messages = messages;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(final Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getPinCount() {
        return pinCount;
    }

    public void setPinCount(final Integer pinCount) {
        this.pinCount = pinCount;
    }

    public ResponseMetadata getResponseMetadata() {
        return responseMetadata;
    }

    public void setResponseMetadata(final ResponseMetadata responseMetadata) {
        this.responseMetadata = responseMetadata;
    }
}
