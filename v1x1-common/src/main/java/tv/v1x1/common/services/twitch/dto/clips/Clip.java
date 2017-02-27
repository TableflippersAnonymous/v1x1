package tv.v1x1.common.services.twitch.dto.clips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Clip {
    @JsonProperty
    private ClipChannel broadcaster;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private ClipChannel curator;
    @JsonProperty
    private double duration;
    @JsonProperty("embed_html")
    private String embedHtml;
    @JsonProperty("embed_url")
    private String embedUrl;
    @JsonProperty
    private String game;
    @JsonProperty
    private String id;
    @JsonProperty
    private ClipThumbnails thumbnails;
    @JsonProperty
    private String title;
    @JsonProperty
    private String url;
    @JsonProperty
    private long views;
    @JsonProperty
    private ClipVod vod;

    public Clip() {
    }

    public ClipChannel getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(final ClipChannel broadcaster) {
        this.broadcaster = broadcaster;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public ClipChannel getCurator() {
        return curator;
    }

    public void setCurator(final ClipChannel curator) {
        this.curator = curator;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(final double duration) {
        this.duration = duration;
    }

    public String getEmbedHtml() {
        return embedHtml;
    }

    public void setEmbedHtml(final String embedHtml) {
        this.embedHtml = embedHtml;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(final String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public String getGame() {
        return game;
    }

    public void setGame(final String game) {
        this.game = game;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ClipThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(final ClipThumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public long getViews() {
        return views;
    }

    public void setViews(final long views) {
        this.views = views;
    }

    public ClipVod getVod() {
        return vod;
    }

    public void setVod(final ClipVod vod) {
        this.vod = vod;
    }
}
