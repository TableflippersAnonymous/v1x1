package tv.v1x1.common.services.twitch.unsupported.tmi.resources;

import tv.v1x1.common.services.twitch.unsupported.tmi.dto.HostResponse;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 11/13/2016.
 */
public class HostsResource {
    private final WebTarget hosts;

    public HostsResource(final WebTarget hosts) {
        this.hosts = hosts;
    }

    public HostResponse getHosts(final long targetId) {
        return hosts
                .queryParam("include_logins", "1")
                .queryParam("target", targetId)
                .request()
                .get()
                .readEntity(HostResponse.class);
    }
}
