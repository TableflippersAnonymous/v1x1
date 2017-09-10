package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Embed {
    @JsonProperty
    private String title;
    @JsonProperty
    private String type;
    @JsonProperty
    private String description;
    @JsonProperty
    private String url;
    @JsonProperty
    private String timestamp;
    @JsonProperty
    private Integer color;
    @JsonProperty
    private EmbedFooter footer;
    @JsonProperty
    private EmbedImage image;
    @JsonProperty
    private EmbedThumbnail thumbnail;
    @JsonProperty
    private EmbedVideo video;
    @JsonProperty
    private EmbedProvider provider;
    @JsonProperty
    private EmbedAuthor author;
    @JsonProperty
    private List<EmbedField> fields;

    public Embed() {
    }

    public Embed(final String title, final String type, final String description, final String url,
                 final String timestamp, final Integer color, final EmbedFooter footer, final EmbedImage image,
                 final EmbedThumbnail thumbnail, final EmbedVideo video, final EmbedProvider provider,
                 final EmbedAuthor author, final List<EmbedField> fields) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.video = video;
        this.provider = provider;
        this.author = author;
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(final Integer color) {
        this.color = color;
    }

    public EmbedFooter getFooter() {
        return footer;
    }

    public void setFooter(final EmbedFooter footer) {
        this.footer = footer;
    }

    public EmbedImage getImage() {
        return image;
    }

    public void setImage(final EmbedImage image) {
        this.image = image;
    }

    public EmbedThumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(final EmbedThumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public EmbedVideo getVideo() {
        return video;
    }

    public void setVideo(final EmbedVideo video) {
        this.video = video;
    }

    public EmbedProvider getProvider() {
        return provider;
    }

    public void setProvider(final EmbedProvider provider) {
        this.provider = provider;
    }

    public EmbedAuthor getAuthor() {
        return author;
    }

    public void setAuthor(final EmbedAuthor author) {
        this.author = author;
    }

    public List<EmbedField> getFields() {
        return fields;
    }

    public void setFields(final List<EmbedField> fields) {
        this.fields = fields;
    }
}
