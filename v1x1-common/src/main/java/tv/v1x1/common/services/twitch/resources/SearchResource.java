package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.channels.ChannelList;
import tv.v1x1.common.services.twitch.dto.games.GameList;
import tv.v1x1.common.services.twitch.dto.misc.SearchType;
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

    public ChannelList findChannels(final String query, final Integer limit, final Integer offset) {
        return search.path("channels")
                .queryParam("query", query)
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(ChannelList.class);
    }

    public StreamList findStreams(final String query, final Integer limit, final Integer offset, final Boolean hls) {
        return search.path("streams")
                .queryParam("query", query)
                .queryParam("limit", limit).queryParam("offset", offset).queryParam("hls", hls)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(StreamList.class);
    }

    public GameList findGames(final String query, final SearchType type, final Boolean live) {
        return search.path("games")
                .queryParam("query", query).queryParam("type", type == null ? null : type.name().toLowerCase())
                .queryParam("live", live)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(GameList.class);
    }
}
