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

    /**
     * Gets games sorted by number of current viewers on Twitch, most popular first.
     * @param limit Maximum number of objects to return. Default: 10. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     */
    public TopGameList getTop(final Integer limit, final Integer offset) {
        return games.path("top")
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(TopGameList.class);
    }
}
