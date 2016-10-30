package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.auth.RootResponse;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/30/2016.
 */
public class RootResource {
    private final WebTarget root;

    public RootResource(final WebTarget root) {
        this.root = root;
    }

    public RootResponse get() {
        return root.request(TwitchApi.ACCEPT).get().readEntity(RootResponse.class);
    }
}
