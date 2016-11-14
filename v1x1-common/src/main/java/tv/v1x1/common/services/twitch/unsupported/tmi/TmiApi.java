package tv.v1x1.common.services.twitch.unsupported.tmi;

import org.glassfish.jersey.jackson.JacksonFeature;
import tv.v1x1.common.services.twitch.unsupported.TwitchUnsupportedRequestFilter;
import tv.v1x1.common.services.twitch.unsupported.tmi.resources.GroupResource;
import tv.v1x1.common.services.twitch.unsupported.tmi.resources.HostsResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 11/13/2016.
 */
public class TmiApi {
    public static final String BASE_URL = "https://tmi.twitch.tv/";

    private final GroupResource group;
    private final HostsResource hosts;

    public TmiApi(final String clientId) {
        final TwitchUnsupportedRequestFilter twitchApiRequestFilter = new TwitchUnsupportedRequestFilter(clientId);
        final Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        client.register(twitchApiRequestFilter);
        final WebTarget api = client.target(BASE_URL);
        group = new GroupResource(api.path("group"));
        hosts = new HostsResource(api.path("hosts"));
    }

    public GroupResource getGroup() {
        return group;
    }

    public HostsResource getHosts() {
        return hosts;
    }
}
