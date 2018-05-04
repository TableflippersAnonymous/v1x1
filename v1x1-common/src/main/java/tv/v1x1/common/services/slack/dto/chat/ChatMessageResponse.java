package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChatMessageResponse extends Response {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty("ts")
    private String messageTs;
    @JsonProperty
    private MessageEvent message;

    public ChatMessageResponse() {
        super(true);
    }

    public ChatMessageResponse(final String channelId, final String messageTs, final MessageEvent message) {
        super(true);
        this.channelId = channelId;
        this.messageTs = messageTs;
        this.message = message;
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

    public MessageEvent getMessage() {
        return message;
    }

    public void setMessage(final MessageEvent message) {
        this.message = message;
    }
}
