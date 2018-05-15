package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.ShortChannel;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("broadcast_id")
    private long broadcastId;
    @JsonProperty("broadcast_type")
    private String broadcastType;
    @JsonProperty
    private ShortChannel channel;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty
    private String description;
    @JsonProperty("description_html")
    private String descriptionHtml;
    @JsonProperty
    private VideoFps fps;
    @JsonProperty
    private String game;
    @JsonProperty
    private String language;
    @JsonProperty
    private int length;
    @JsonProperty
    private VideoPreview preview;
    @JsonProperty("published_at")
    private String publishedAt;
    @JsonProperty
    private VideoResolutions resolutions;
    @JsonProperty
    private String status;
    @JsonProperty("tag_list")
    private String tagList;
    @JsonProperty
    private VideoThumbnails thumbnails;
    @JsonProperty
    private String title;
    @JsonProperty
    private String url;
    @JsonProperty
    private String viewable;
    @JsonProperty
    private String viewableAt;
    @JsonProperty
    private long views;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public long getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(final long broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(final String tagList) {
        this.tagList = tagList;
    }

    public String getGame() {
        return game;
    }

    public void setGame(final String game) {
        this.game = game;
    }

    public int getLength() {
        return length;
    }

    public void setLength(final int length) {
        this.length = length;
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

    public String getBroadcastType() {
        return broadcastType;
    }

    public void setBroadcastType(final String broadcastType) {
        this.broadcastType = broadcastType;
    }

    public ShortChannel getChannel() {
        return channel;
    }

    public void setChannel(final ShortChannel channel) {
        this.channel = channel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(final String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public VideoFps getFps() {
        return fps;
    }

    public void setFps(final VideoFps fps) {
        this.fps = fps;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public VideoPreview getPreview() {
        return preview;
    }

    public void setPreview(final VideoPreview preview) {
        this.preview = preview;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(final String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public VideoResolutions getResolutions() {
        return resolutions;
    }

    public void setResolutions(final VideoResolutions resolutions) {
        this.resolutions = resolutions;
    }

    public VideoThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(final VideoThumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getViewable() {
        return viewable;
    }

    public void setViewable(final String viewable) {
        this.viewable = viewable;
    }

    public String getViewableAt() {
        return viewableAt;
    }

    public void setViewableAt(final String viewableAt) {
        this.viewableAt = viewableAt;
    }
}
