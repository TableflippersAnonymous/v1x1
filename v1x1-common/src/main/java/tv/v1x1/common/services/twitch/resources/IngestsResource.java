package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.ingests.IngestList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 10/30/2016.
 */
public class IngestsResource {
    private final WebTarget ingests;

    public IngestsResource(final WebTarget ingests) {
        this.ingests = ingests;
    }

    /**
     * Gets a list of Twitch ingest servers. The Twitch ingesting system is the first stop for a broadcast stream. An
     * ingest server receives your stream, and the ingesting system authorizes and registers streams, then prepares
     * them for viewers.
     */
    public IngestList getIngests() {
        return ingests.request(TwitchApi.ACCEPT).get(IngestList.class);
    }
}
