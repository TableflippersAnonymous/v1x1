package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatMessageRequest {
    @JsonProperty("channel")
    private String channelId;
    @JsonProperty
    private String text;
    @JsonProperty("as_user")
    private Boolean asUser;
    @JsonProperty
    private List<Attachment> attachments;
    @JsonProperty("icon_emoji")
    private String iconEmoji;
    @JsonProperty("icon_url")
    private String iconUrl;
    @JsonProperty("link_names")
    private Boolean linkNames;
    @JsonProperty("mrkdwn")
    private Boolean markdown;
    @JsonProperty
    private ParseType parse;
    @JsonProperty("reply_broadcast")
    private Boolean replyBroadcast;
    @JsonProperty("thread_ts")
    private String threadTs;
    @JsonProperty("unfurl_links")
    private Boolean unfurlLinks;
    @JsonProperty("unfurl_media")
    private Boolean unfurlMedia;
    @JsonProperty
    private String username;

    public ChatMessageRequest() {
    }

    public ChatMessageRequest(final String channelId, final String text, final Boolean asUser,
                              final List<Attachment> attachments, final String iconEmoji, final String iconUrl,
                              final Boolean linkNames, final Boolean markdown, final ParseType parse,
                              final Boolean replyBroadcast, final String threadTs, final Boolean unfurlLinks,
                              final Boolean unfurlMedia, final String username) {
        this.channelId = channelId;
        this.text = text;
        this.asUser = asUser;
        this.attachments = attachments;
        this.iconEmoji = iconEmoji;
        this.iconUrl = iconUrl;
        this.linkNames = linkNames;
        this.markdown = markdown;
        this.parse = parse;
        this.replyBroadcast = replyBroadcast;
        this.threadTs = threadTs;
        this.unfurlLinks = unfurlLinks;
        this.unfurlMedia = unfurlMedia;
        this.username = username;
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

    public String getIconEmoji() {
        return iconEmoji;
    }

    public void setIconEmoji(final String iconEmoji) {
        this.iconEmoji = iconEmoji;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(final String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getLinkNames() {
        return linkNames;
    }

    public void setLinkNames(final Boolean linkNames) {
        this.linkNames = linkNames;
    }

    public Boolean getMarkdown() {
        return markdown;
    }

    public void setMarkdown(final Boolean markdown) {
        this.markdown = markdown;
    }

    public ParseType getParse() {
        return parse;
    }

    public void setParse(final ParseType parse) {
        this.parse = parse;
    }

    public Boolean getReplyBroadcast() {
        return replyBroadcast;
    }

    public void setReplyBroadcast(final Boolean replyBroadcast) {
        this.replyBroadcast = replyBroadcast;
    }

    public String getThreadTs() {
        return threadTs;
    }

    public void setThreadTs(final String threadTs) {
        this.threadTs = threadTs;
    }

    public Boolean getUnfurlLinks() {
        return unfurlLinks;
    }

    public void setUnfurlLinks(final Boolean unfurlLinks) {
        this.unfurlLinks = unfurlLinks;
    }

    public Boolean getUnfurlMedia() {
        return unfurlMedia;
    }

    public void setUnfurlMedia(final Boolean unfurlMedia) {
        this.unfurlMedia = unfurlMedia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
