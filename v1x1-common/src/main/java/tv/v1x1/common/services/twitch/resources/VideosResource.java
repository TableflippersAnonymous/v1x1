package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.misc.Period;
import tv.v1x1.common.services.twitch.dto.streams.BroadcastType;
import tv.v1x1.common.services.twitch.dto.videos.TotalledVideoList;
import tv.v1x1.common.services.twitch.dto.videos.Video;
import tv.v1x1.common.services.twitch.dto.videos.VideoList;
import tv.v1x1.common.services.twitch.dto.videos.VodList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/30/2016.
 */
public class VideosResource {
    private final WebTarget videos;

    public VideosResource(final WebTarget videos) {
        this.videos = videos;
    }

    /**
     * Gets a specified video object.
     * Note that in prior versions of the API, the specified video ID required a “v” prefix. That prefix is deprecated.
     * @param videoId ID of video
     */
    public Video getVideo(final String videoId) {
        return videos.path(videoId)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Video.class);
    }

    /**
     * Gets the top videos based on viewcount, optionally filtered by game or time period.
     * @param limit Maximum number of objects to return. Default: 10. Maximum: 100.
     * @param offset Object offset for pagination of reults. Default: 0.
     * @param game Constrains videos by game. A game name can be retrieved using the Search Games endpoint.
     * @param period Specifies the window of time to search. Valid values: week, month, all. Default: week
     * @param broadcastType Constrains the type of videos returned. Valid values: (any combination of) archive,
     *                      highlight, upload, Default: highlight.
     */
    public VodList getTop(final Integer limit, final Integer offset, final String game, final Period period, final BroadcastType broadcastType) {
        return videos.path("top")
                .queryParam("limit", limit).queryParam("offset", offset).queryParam("game", game)
                .queryParam("period", period == null ? null : period.name().toLowerCase())
                .queryParam("broadcast_type", broadcastType == null ? null : broadcastType.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(VodList.class);
    }

    /**
     * Gets the videos from channels followed by a user, based on a specified OAuth token.
     * @param limit Maximum number of objects to return, sorted by creation date. Default: 10. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param broadcastType Constrains the type of videos returned. Valid values: (any combination of) archive,
     *                      highlight, upload, Default: highlight.
     */
    public VideoList getFollowedVideos(final Integer limit, final Integer offset, final BroadcastType broadcastType) {
        return videos.path("followed")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("broadcast_type", broadcastType == null ? null : broadcastType.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(VideoList.class);
    }
}
