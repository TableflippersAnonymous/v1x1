package tv.v1x1.common.services.twitch.unsupported.tmi.resources;

import tv.v1x1.common.services.twitch.unsupported.tmi.dto.ChatterResponse;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 11/13/2016.
 */
public class GroupResource {
    private final WebTarget group;

    public GroupResource(final WebTarget group) {
        this.group = group;
    }

    public ChatterResponse getChatters(final String username) {
        return group.path("user").path(username).path("chatters").request().get().readEntity(ChatterResponse.class);
    }
}
