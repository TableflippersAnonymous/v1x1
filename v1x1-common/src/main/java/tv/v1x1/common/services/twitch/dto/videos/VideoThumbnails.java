package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoThumbnails {
    @JsonProperty
    private List<VideoThumbnail> large;
    @JsonProperty
    private List<VideoThumbnail> medium;
    @JsonProperty
    private List<VideoThumbnail> small;
    @JsonProperty
    private List<VideoThumbnail> template;

    public VideoThumbnails() {
    }

    public List<VideoThumbnail> getLarge() {
        return large;
    }

    public void setLarge(final List<VideoThumbnail> large) {
        this.large = large;
    }

    public List<VideoThumbnail> getMedium() {
        return medium;
    }

    public void setMedium(final List<VideoThumbnail> medium) {
        this.medium = medium;
    }

    public List<VideoThumbnail> getSmall() {
        return small;
    }

    public void setSmall(final List<VideoThumbnail> small) {
        this.small = small;
    }

    public List<VideoThumbnail> getTemplate() {
        return template;
    }

    public void setTemplate(final List<VideoThumbnail> template) {
        this.template = template;
    }
}
