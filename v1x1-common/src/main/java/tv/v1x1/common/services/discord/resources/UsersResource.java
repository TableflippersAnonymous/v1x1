package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.common.services.discord.dto.guild.PartialGuild;
import tv.v1x1.common.services.discord.dto.user.Connection;
import tv.v1x1.common.services.discord.dto.user.CreateDMRequest;
import tv.v1x1.common.services.discord.dto.user.CreateGroupDMRequest;
import tv.v1x1.common.services.discord.dto.user.ModifyCurrentUserRequest;
import tv.v1x1.common.services.discord.dto.user.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by naomi on 9/17/2017.
 */
public class UsersResource {
    private final WebTarget users;

    public UsersResource(final WebTarget users) {
        this.users = users;
    }

    public User getCurrentUser() {
        return users.path("@me")
                .request(DiscordApi.ACCEPT)
                .get(User.class);
    }

    public User getUser(final String userId) {
        return users.path(userId)
                .request(DiscordApi.ACCEPT)
                .get(User.class);
    }

    public User modifyCurrentUser(final ModifyCurrentUserRequest modifyCurrentUserRequest) {
        return users.path("@me")
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyCurrentUserRequest, MediaType.APPLICATION_JSON), User.class);
    }

    public List<PartialGuild> getCurrentUserGuilds(final String before, final String after, final Integer limit) {
        return users.path("@me").path("guilds")
                .queryParam("before", before).queryParam("after", after)
                .queryParam("limit", limit)
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<PartialGuild>>() {});
    }

    public void leaveGuild(final String guildId) {
        users.path("@me").path("guilds").path(guildId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public List<Channel> getUserDMs() {
        return users.path("@me").path("channels")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Channel>>() {});
    }

    public Channel createDM(final CreateDMRequest createDMRequest) {
        return users.path("@me").path("channels")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createDMRequest, MediaType.APPLICATION_JSON), Channel.class);
    }

    public Channel createGroupDM(final CreateGroupDMRequest createGroupDMRequest) {
        return users.path("@me").path("channels")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createGroupDMRequest, MediaType.APPLICATION_JSON), Channel.class);
    }

    public List<Connection> getUserConnections() {
        return users.path("@me").path("connections")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Connection>>() {});
    }
}
