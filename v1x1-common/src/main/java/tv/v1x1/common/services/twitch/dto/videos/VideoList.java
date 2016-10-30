package tv.v1x1.common.services.twitch.dto.videos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 10/30/2016.
 */
public class VideoList {
    @JsonProperty
    private List<Video> videos;

    public VideoList() {
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(final List<Video> videos) {
        this.videos = videos;
    }
}
