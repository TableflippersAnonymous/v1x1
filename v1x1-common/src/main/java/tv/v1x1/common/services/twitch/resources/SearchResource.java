package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.channels.ChannelList;
import tv.v1x1.common.services.twitch.dto.games.GameList;
import tv.v1x1.common.services.twitch.dto.streams.StreamList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 10/30/2016.
 */
public class SearchResource {
    private final WebTarget search;

    public SearchResource(final WebTarget search) {
        this.search = search;
    }

    /**
     * Searches for channels based on a specified query parameter. A channel is returned if the query parameter is
     * matched entirely or partially, in the channel description or game name.
     * @param query Query to search on
     * @param limit Maximum number of objects to return, sorted by number of followers. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @return
     */
    public ChannelList findChannels(final String query, final Integer limit, final Integer offset) {
        return search.path("channels")
                .queryParam("query", query)
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get(ChannelList.class);
    }

    /**
     * Searches for games based on a specified query parameter. A game is returned if the query parameter is matched
     * entirely or partially, in the game name.
     * @param query Query to search on
     * @param live If true, returns only games that are live on at least one channel. Default: false.
     */
    public GameList findGames(final String query, final Boolean live) {
        return search.path("games")
                .queryParam("query", query)
                .queryParam("live", live)
                .request(TwitchApi.ACCEPT)
                .get(GameList.class);
    }

    /**
     * Searches for streams based on a specified query parameter. A stream is returned if the query parameter is matched
     * entirely or partially, in the channel description or game name.
     * @param query Query to search on
     * @param limit Maximum number of objects to return, sorted by number of current viewers. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param hls If true, returns only HLS streams; if false, only RTMP streams; if not set, both HLS and RTMP streams.
     *            HLS is HTTP Live Streaming, a live-streaming communications protocol. RTMP is Real-Time Media
     *            Protocol, an industry standard for moving video around a network. Default: not set.
     */
    public StreamList findStreams(final String query, final Integer limit, final Integer offset, final Boolean hls) {
        return search.path("streams")
                .queryParam("query", query)
                .queryParam("limit", limit).queryParam("offset", offset).queryParam("hls", hls)
                .request(TwitchApi.ACCEPT)
                .get(StreamList.class);
    }
}
