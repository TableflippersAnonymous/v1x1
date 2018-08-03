package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.invite.Invite;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 9/17/2017.
 */
public class InvitesResource {
    private final WebTarget invites;

    public InvitesResource(final WebTarget invites) {
        this.invites = invites;
    }

    public Invite getInvite(final String inviteCode) {
        return invites.path(inviteCode)
                .request(DiscordApi.ACCEPT)
                .get(Invite.class);
    }

    public Invite deleteInvite(final String inviteCode) {
        return invites.path(inviteCode)
                .request(DiscordApi.ACCEPT)
                .delete(Invite.class);
    }

    public Invite acceptInvite(final String inviteCode) {
        return invites.path(inviteCode)
                .request(DiscordApi.ACCEPT)
                .post(null, Invite.class);
    }
}
