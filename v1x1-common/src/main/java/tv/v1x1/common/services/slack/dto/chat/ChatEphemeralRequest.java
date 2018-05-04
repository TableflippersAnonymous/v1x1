package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatEphemeralRequest {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty
    private String text;
    @JsonProperty("user")
    private String userId;
    @JsonProperty("as_user")
    private Boolean asUser;
    @JsonProperty
    private List<Attachment> attachments;
    @JsonProperty("link_names")
    private Boolean linkNames;
    @JsonProperty
    private ParseType parse;

    public ChatEphemeralRequest() {
    }

    public ChatEphemeralRequest(final String channelId, final String text, final String userId, final Boolean asUser,
                                final List<Attachment> attachments, final Boolean linkNames, final ParseType parse) {
        this.channelId = channelId;
        this.text = text;
        this.userId = userId;
        this.asUser = asUser;
        this.attachments = attachments;
        this.linkNames = linkNames;
        this.parse = parse;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public Boolean getAsUser() {
        return asUser;
    }

    public void setAsUser(final Boolean asUser) {
        this.asUser = asUser;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Boolean getLinkNames() {
        return linkNames;
    }

    public void setLinkNames(final Boolean linkNames) {
        this.linkNames = linkNames;
    }

    public ParseType getParse() {
        return parse;
    }

    public void setParse(final ParseType parse) {
        this.parse = parse;
    }
}
