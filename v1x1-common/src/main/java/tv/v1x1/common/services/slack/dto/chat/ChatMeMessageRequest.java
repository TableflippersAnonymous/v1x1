package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMeMessageRequest {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty
    private String text;

    public ChatMeMessageRequest() {
    }

    public ChatMeMessageRequest(final String channelId, final String text) {
        this.channelId = channelId;
        this.text = text;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
