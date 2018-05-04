package tv.v1x1.common.services.slack.dto.chat;

import java.util.List;

public class ChatMessageRequestBuilder {
    private String channelId;
    private String text;
    private Boolean asUser;
    private List<Attachment> attachments;
    private String iconEmoji;
    private String iconUrl;
    private Boolean linkNames;
    private Boolean markdown;
    private ParseType parse;
    private Boolean replyBroadcast;
    private String threadTs;
    private Boolean unfurlLinks;
    private Boolean unfurlMedia;
    private String username;

    public ChatMessageRequestBuilder setChannelId(final String channelId) {
        this.channelId = channelId;
        return this;
    }

    public ChatMessageRequestBuilder setText(final String text) {
        this.text = text;
        return this;
    }

    public ChatMessageRequestBuilder setAsUser(final Boolean asUser) {
        this.asUser = asUser;
        return this;
    }

    public ChatMessageRequestBuilder setAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public ChatMessageRequestBuilder setIconEmoji(final String iconEmoji) {
        this.iconEmoji = iconEmoji;
        return this;
    }

    public ChatMessageRequestBuilder setIconUrl(final String iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public ChatMessageRequestBuilder setLinkNames(final Boolean linkNames) {
        this.linkNames = linkNames;
        return this;
    }

    public ChatMessageRequestBuilder setMarkdown(final Boolean markdown) {
        this.markdown = markdown;
        return this;
    }

    public ChatMessageRequestBuilder setParse(final ParseType parse) {
        this.parse = parse;
        return this;
    }

    public ChatMessageRequestBuilder setReplyBroadcast(final Boolean replyBroadcast) {
        this.replyBroadcast = replyBroadcast;
        return this;
    }

    public ChatMessageRequestBuilder setThreadTs(final String threadTs) {
        this.threadTs = threadTs;
        return this;
    }

    public ChatMessageRequestBuilder setUnfurlLinks(final Boolean unfurlLinks) {
        this.unfurlLinks = unfurlLinks;
        return this;
    }

    public ChatMessageRequestBuilder setUnfurlMedia(final Boolean unfurlMedia) {
        this.unfurlMedia = unfurlMedia;
        return this;
    }

    public ChatMessageRequestBuilder setUsername(final String username) {
        this.username = username;
        return this;
    }

    public ChatMessageRequest createChatMessageRequest() {
        return new ChatMessageRequest(channelId, text, asUser, attachments, iconEmoji, iconUrl, linkNames, markdown, parse, replyBroadcast, threadTs, unfurlLinks, unfurlMedia, username);
    }
}