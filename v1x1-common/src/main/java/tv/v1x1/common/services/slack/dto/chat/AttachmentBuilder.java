package tv.v1x1.common.services.slack.dto.chat;

import java.util.List;

public class AttachmentBuilder {
    private String fallback;
    private String color;
    private String pretext;
    private String authorName;
    private String authorLink;
    private String authorIcon;
    private String title;
    private String titleLink;
    private String text;
    private List<Attachment.Field> fields;
    private String imageUrl;
    private String thumbUrl;
    private String footer;
    private String footerIcon;
    private Long timestamp;

    public AttachmentBuilder setFallback(final String fallback) {
        this.fallback = fallback;
        return this;
    }

    public AttachmentBuilder setColor(final String color) {
        this.color = color;
        return this;
    }

    public AttachmentBuilder setPretext(final String pretext) {
        this.pretext = pretext;
        return this;
    }

    public AttachmentBuilder setAuthorName(final String authorName) {
        this.authorName = authorName;
        return this;
    }

    public AttachmentBuilder setAuthorLink(final String authorLink) {
        this.authorLink = authorLink;
        return this;
    }

    public AttachmentBuilder setAuthorIcon(final String authorIcon) {
        this.authorIcon = authorIcon;
        return this;
    }

    public AttachmentBuilder setTitle(final String title) {
        this.title = title;
        return this;
    }

    public AttachmentBuilder setTitleLink(final String titleLink) {
        this.titleLink = titleLink;
        return this;
    }

    public AttachmentBuilder setText(final String text) {
        this.text = text;
        return this;
    }

    public AttachmentBuilder setFields(final List<Attachment.Field> fields) {
        this.fields = fields;
        return this;
    }

    public AttachmentBuilder setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public AttachmentBuilder setThumbUrl(final String thumbUrl) {
        this.thumbUrl = thumbUrl;
        return this;
    }

    public AttachmentBuilder setFooter(final String footer) {
        this.footer = footer;
        return this;
    }

    public AttachmentBuilder setFooterIcon(final String footerIcon) {
        this.footerIcon = footerIcon;
        return this;
    }

    public AttachmentBuilder setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Attachment createAttachment() {
        return new Attachment(fallback, color, pretext, authorName, authorLink, authorIcon, title, titleLink, text,
                fields, imageUrl, thumbUrl, footer, footerIcon, timestamp);
    }
}