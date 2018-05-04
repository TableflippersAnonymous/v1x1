package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.slack.dto.Response;

public class ChatEphemeralResponse extends Response {
    @JsonProperty("message_ts")
    private String messageTs;

    public ChatEphemeralResponse() {
        super(true);
    }

    public ChatEphemeralResponse(final String messageTs) {
        super(true);
        this.messageTs = messageTs;
    }

    public String getMessageTs() {
        return messageTs;
    }

    public void setMessageTs(final String messageTs) {
        this.messageTs = messageTs;
    }
}
