package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.misc.Period;
import tv.v1x1.common.services.twitch.dto.videos.TotalledVideoList;
import tv.v1x1.common.services.twitch.dto.videos.Video;
import tv.v1x1.common.services.twitch.dto.videos.VideoList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 10/30/2016.
 */
public class VideosResource {
    private final WebTarget videos;
    private final WebTarget channels;

    public VideosResource(final WebTarget videos, final WebTarget channels) {
        this.videos = videos;
        this.channels = channels;
    }

    public Video getVideo(final String id) {
        return videos.path(id)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Video.class);
    }

    public VideoList getTop(final Integer limit, final Integer offset, final String game, final Period period) {
        return videos.path("top")
                .queryParam("limit", limit).queryParam("offset", offset).queryParam("game", game)
                .queryParam("period", period == null ? null : period.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(VideoList.class);
    }

    public TotalledVideoList getVideos(final String channel, final Integer limit, final Integer offset, final Boolean broadcasts, final Boolean hls) {
        return channels.path(channel).path("videos")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("broadcasts", broadcasts).queryParam("hls", hls)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(TotalledVideoList.class);
    }
}
