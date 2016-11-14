package tv.v1x1.common.services.twitch.unsupported.tmi;

import tv.v1x1.common.services.twitch.unsupported.TwitchUnsupportedRequestFilter;
import tv.v1x1.common.services.twitch.unsupported.tmi.resources.GroupResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 11/13/2016.
 */
public class TmiApi {
    public static final String BASE_URL = "https://tmi.twitch.tv/";

    private final GroupResource group;

    public TmiApi(final String clientId) {
        final TwitchUnsupportedRequestFilter twitchApiRequestFilter = new TwitchUnsupportedRequestFilter(clientId);
        final Client client = ClientBuilder.newClient();
        client.register(twitchApiRequestFilter);
        final WebTarget api = client.target(BASE_URL);
        group = new GroupResource(api.path("group"));
    }

    public GroupResource getGroup() {
        return group;
    }
}
