package tv.v1x1.common.services.slack.dto.conversations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversationCloseRequest {
    @JsonProperty("channel")
    private String channelId;

    public ConversationCloseRequest() {
    }

    public ConversationCloseRequest(final String channelId) {
        this.channelId = channelId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }
}
