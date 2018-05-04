package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChatUpdateResponse extends Response {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty("ts")
    private String messageTs;
    @JsonProperty
    private String text;

    public ChatUpdateResponse() {
        super(true);
    }

    public ChatUpdateResponse(final String channelId, final String messageTs, final String text) {
        super(true);
        this.channelId = channelId;
        this.messageTs = messageTs;
        this.text = text;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getMessageTs() {
        return messageTs;
    }

    public void setMessageTs(final String messageTs) {
        this.messageTs = messageTs;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }
}
