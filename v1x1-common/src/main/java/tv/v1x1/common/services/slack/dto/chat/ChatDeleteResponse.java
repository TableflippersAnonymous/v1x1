package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChatDeleteResponse extends Response {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty("ts")
    private String messageTs;

    public ChatDeleteResponse() {
        super(true);
    }

    public ChatDeleteResponse(final String channelId, final String messageTs) {
        super(true);
        this.channelId = channelId;
        this.messageTs = messageTs;
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
}
