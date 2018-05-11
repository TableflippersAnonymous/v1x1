package tv.v1x1.common.services.slack.dto.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ConversationInfoResponse extends Response {
    @JsonProperty
    private Conversation channel;

    public ConversationInfoResponse() {
        super(true);
    }

    public ConversationInfoResponse(final Conversation channel) {
        super(true);
        this.channel = channel;
    }

    public Conversation getChannel() {
        return channel;
    }

    public void setChannel(final Conversation channel) {
        this.channel = channel;
    }
}
