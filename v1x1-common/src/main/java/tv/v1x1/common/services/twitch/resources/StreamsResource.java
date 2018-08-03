package tv.v1x1.common.services.twitch.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.streams.FeaturedStreamList;
import tv.v1x1.common.services.twitch.dto.streams.StreamList;
import tv.v1x1.common.services.twitch.dto.streams.StreamResponse;
import tv.v1x1.common.services.twitch.dto.streams.StreamSummary;
import tv.v1x1.common.services.twitch.dto.streams.StreamType;

import javax.ws.rs.client.WebTarget;
import java.util.List;

/**
 * Created by cobi on 10/30/2016.
 */
public class StreamsResource {
    private final WebTarget streams;

    public StreamsResource(final WebTarget streams) {
        this.streams = streams;
    }

    /**
     * Gets stream information (the stream object) for a specified user.
     * @param channelId ID of channel
     */
    public StreamResponse getStream(final String channelId) {
        return streams.path(channelId).request(TwitchApi.ACCEPT).get(StreamResponse.class);
    }

    /**
     * Gets stream information (the stream object) for a specified user.
     * @param channelId ID of channel
     * @param streamType Constrains the type of streams returned. Valid values: live, playlist, all. Playlists are
     *                   offline streams of VODs (Video on Demand) that appear live. Default: live.
     */
    public StreamResponse getStream(final String channelId, final String streamType) {
        return streams.path(channelId)
                .queryParam("stream_type", streamType)
                .request(TwitchApi.ACCEPT)
                .get(StreamResponse.class);
    }

    /**
     * Gets a list of live streams.
     * @param game Constrains the game of the streams returned.
     * @param channelIds Constrains the channel(s) of the streams returned.
     * @param limit Maximum number of objects to return, sorted by number of viewers. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param streamType Constrains the type of streams returned. Valid values: live, playlist, all. Playlists are
     *                   offline streams of VODs (Video on Demand) that appear live. Default: live.
     * @param language Constrains the language of the streams returned. Valid value: a locale ID string; for example,
     *                 en, fi, es-mx. Only one language can be specified. Default: all languages.
     */
    public StreamList listStreams(final String game, final List<String> channelIds, final Integer limit, final Integer offset, final StreamType streamType, final String language) {
        return streams
                .queryParam("game", game)
                .queryParam("channels", channelIds == null ? null : Joiner.on(",").join(channelIds))
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("stream_type", streamType == null ? null : streamType.name().toLowerCase())
                .queryParam("language", language)
                .request(TwitchApi.ACCEPT)
                .get(StreamList.class);
    }

    /**
     * Gets a summary of live streams.
     * @param game Constrains the game of the streams returned.
     */
    public StreamSummary getSummary(final String game) {
        return streams.path("summary")
                .queryParam("game", game)
                .request(TwitchApi.ACCEPT)
                .get(StreamSummary.class);
    }

    /**
     * Gets a list of all featured live streams.
     * @param limit Maximum number of objects to return, sorted by priority. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     */
    public FeaturedStreamList getFeatured(final Integer limit, final Integer offset) {
        return streams.path("featured")
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get(FeaturedStreamList.class);
    }

    /**
     * Gets a list of online streams a user is following, based on a specified OAuth token.
     * @param limit Maximum number of objects to return. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param streamType Constrains the type of streams returned. Valid values: live, playlist, all. Playlists are
     *                   offline streams of VODs (Video on Demand) that appear live. Default: live.
     */
    public StreamList getFollowedStreams(final Integer limit, final Integer offset, final StreamType streamType) {
        return streams.path("followed")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("stream_type", streamType == null ? null : streamType.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get(StreamList.class);
    }
}
