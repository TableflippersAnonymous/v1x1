package tv.v1x1.common.services.slack.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Attachment {
    public static class Field {
        @JsonProperty
        private String title;
        @JsonProperty
        private String value;
        @JsonProperty("short")
        private Boolean isShort;

        public Field() {
        }

        public Field(final String title, final String value, final Boolean isShort) {
            this.title = title;
            this.value = value;
            this.isShort = isShort;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(final String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public Boolean getShort() {
            return isShort;
        }

        public void setShort(final Boolean aShort) {
            isShort = aShort;
        }
    }

    @JsonProperty
    private String fallback;
    @JsonProperty
    private String color;
    @JsonProperty
    private String pretext;
    @JsonProperty("author_name")
    private String authorName;
    @JsonProperty("author_link")
    private String authorLink;
    @JsonProperty("author_icon")
    private String authorIcon;
    @JsonProperty
    private String title;
    @JsonProperty("title_link")
    private String titleLink;
    @JsonProperty
    private String text;
    @JsonProperty
    private List<Field> fields;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("thumb_url")
    private String thumbUrl;
    @JsonProperty
    private String footer;
    @JsonProperty("footer_icon")
    private String footerIcon;
    @JsonProperty("ts")
    private Long timestamp;

    public Attachment() {
    }

    public Attachment(final String fallback, final String color, final String pretext, final String authorName,
                      final String authorLink, final String authorIcon, final String title, final String titleLink,
                      final String text, final List<Field> fields, final String imageUrl, final String thumbUrl,
                      final String footer, final String footerIcon, final Long timestamp) {
        this.fallback = fallback;
        this.color = color;
        this.pretext = pretext;
        this.authorName = authorName;
        this.authorLink = authorLink;
        this.authorIcon = authorIcon;
        this.title = title;
        this.titleLink = titleLink;
        this.text = text;
        this.fields = fields;
        this.imageUrl = imageUrl;
        this.thumbUrl = thumbUrl;
        this.footer = footer;
        this.footerIcon = footerIcon;
        this.timestamp = timestamp;
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(final String fallback) {
        this.fallback = fallback;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getPretext() {
        return pretext;
    }

    public void setPretext(final String pretext) {
        this.pretext = pretext;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorLink() {
        return authorLink;
    }

    public void setAuthorLink(final String authorLink) {
        this.authorLink = authorLink;
    }

    public String getAuthorIcon() {
        return authorIcon;
    }

    public void setAuthorIcon(final String authorIcon) {
        this.authorIcon = authorIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(final String titleLink) {
        this.titleLink = titleLink;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(final List<Field> fields) {
        this.fields = fields;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(final String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(final String footer) {
        this.footer = footer;
    }

    public String getFooterIcon() {
        return footerIcon;
    }

    public void setFooterIcon(final String footerIcon) {
        this.footerIcon = footerIcon;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Long timestamp) {
        this.timestamp = timestamp;
    }
}
