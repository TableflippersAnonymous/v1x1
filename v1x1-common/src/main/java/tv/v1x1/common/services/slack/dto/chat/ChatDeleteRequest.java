package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatDeleteRequest {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty("ts")
    private String messageTs;
    @JsonProperty("as_user")
    private Boolean asUser;

    public ChatDeleteRequest() {
    }

    public ChatDeleteRequest(final String channelId, final String messageTs, final Boolean asUser) {
        this.channelId = channelId;
        this.messageTs = messageTs;
        this.asUser = asUser;
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

    public Boolean getAsUser() {
        return asUser;
    }

    public void setAsUser(final Boolean asUser) {
        this.asUser = asUser;
    }
}
