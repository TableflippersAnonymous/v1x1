package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.clips.Clip;
import tv.v1x1.common.services.twitch.dto.clips.ClipList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 2/28/2017.
 */
public class ClipsResource {
    private final WebTarget clips;

    public ClipsResource(final WebTarget clips) {
        this.clips = clips;
    }

    /**
     * Gets details about a specified Clip. Clips are referenced by:
     * The name of the channel where they were created.
     * A randomly-generated string called a slug.
     * @param channelName Channel name (not ID) of channel where the clip was created
     * @param slug URL slug of clip
     */
    public Clip getClip(final String channelName, final String slug) {
        return clips.path(channelName).path(slug)
                .request(TwitchApi.ACCEPT_V4)
                .get()
                .readEntity(Clip.class);
    }

    /**
     * Gets the top Clips which meet a specified set of parameters.
     * @param channel Comma-separated list of channel names. If this is not specified, top Clips for all channels are
     *                returned. Maximum: 10.
     * @param cursor Tells the server where to start fetching the next set of results, in a multi-page response.
     * @param game Comma-separated list of game names. A game can be retrieved using the games search API. If this is
     *             not specified, top Clips for all games are returned. Maximum: 10.
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 500.
     * @param period The window of time to search for Clips. Valid values: day, week, month, all. Default: day.
     * @param trending If true, the Clips returned are ordered by popularity; otherwise, by viewcount. Default: false.
     */
    public ClipList getTop(final String channel, final String cursor, final String game, final long limit, final String period, final boolean trending) {
        return clips.path("top")
                .queryParam("channel", channel).queryParam("cursor", cursor).queryParam("game", game)
                .queryParam("limit", limit).queryParam("period", period).queryParam("trending", trending)
                .request(TwitchApi.ACCEPT_V4)
                .get()
                .readEntity(ClipList.class);
    }

    /**
     * Gets the top Clips for the games followed by a specified user, identified by an OAuth token.
     * @param limit Maximum number of most-recent objects to return. Default: 10. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results, in a multi-page response.
     * @param trending If true, the Clips returned are ordered by popularity; otherwise, by viewcount. Default: false.
     */
    public ClipList getFollowed(final long limit, final String cursor, final boolean trending) {
        return clips.path("followed")
                .queryParam("limit", limit).queryParam("cursor", cursor).queryParam("trending", trending)
                .request(TwitchApi.ACCEPT_V4)
                .get()
                .readEntity(ClipList.class);
    }
}
