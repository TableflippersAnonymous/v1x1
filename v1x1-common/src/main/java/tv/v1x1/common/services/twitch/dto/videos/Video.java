package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.channels.ShortChannel;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty("broadcast_id")
    private long broadcastId;
    @JsonProperty
    private String status;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("tag_list")
    private String tagList;
    @JsonProperty("recorded_at")
    private String recordedAt;
    @JsonProperty
    private String game;
    @JsonProperty
    private int length;
    @JsonProperty
    private String preview;
    @JsonProperty
    private String url;
    @JsonProperty
    private long views;
    @JsonProperty("broadcast_type")
    private String broadcastType;
    @JsonProperty
    private ShortChannel channel;

    public Video() {
    }

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

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(final String recordedAt) {
        this.recordedAt = recordedAt;
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

    public String getPreview() {
        return preview;
    }

    public void setPreview(final String preview) {
        this.preview = preview;
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
}
