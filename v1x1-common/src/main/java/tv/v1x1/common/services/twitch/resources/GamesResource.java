package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.games.TopGameList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/30/2016.
 */
public class GamesResource {
    private final WebTarget games;

    public GamesResource(final WebTarget games) {
        this.games = games;
    }

    public TopGameList getTop(final Integer limit, final Integer offset) {
        return games.path("top")
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(TopGameList.class);
    }
}
