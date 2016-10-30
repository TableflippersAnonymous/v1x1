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
 * Created by naomi on 10/30/2016.
 */
public class StreamsResource {
    private final WebTarget streams;

    public StreamsResource(final WebTarget streams) {
        this.streams = streams;
    }

    public StreamResponse getStream(final String channel) {
        return streams.path(channel).request(TwitchApi.ACCEPT).get().readEntity(StreamResponse.class);
    }

    public StreamList listStreams(final String game, final List<String> channels, final Integer limit, final Integer offset, final StreamType streamType, final String language) {
        return streams
                .queryParam("game", game)
                .queryParam("channels", channels == null ? null : Joiner.on(",").join(channels))
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .queryParam("stream_type", streamType == null ? null : streamType.name().toLowerCase())
                .queryParam("language", language)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(StreamList.class);
    }

    public FeaturedStreamList getFeatured(final Integer limit, final Integer offset) {
        return streams.path("featured")
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(FeaturedStreamList.class);
    }

    public StreamSummary getSummary(final String game) {
        return streams.path("summary")
                .queryParam("game", game)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(StreamSummary.class);
    }
}
