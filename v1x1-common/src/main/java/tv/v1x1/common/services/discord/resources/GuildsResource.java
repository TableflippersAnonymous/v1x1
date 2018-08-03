package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.common.services.discord.dto.guild.AddGuildMemberRequest;
import tv.v1x1.common.services.discord.dto.guild.Ban;
import tv.v1x1.common.services.discord.dto.guild.CreateGuildChannelRequest;
import tv.v1x1.common.services.discord.dto.guild.CreateGuildIntegrationRequest;
import tv.v1x1.common.services.discord.dto.guild.CreateGuildRequest;
import tv.v1x1.common.services.discord.dto.guild.CreateGuildRoleRequest;
import tv.v1x1.common.services.discord.dto.guild.Guild;
import tv.v1x1.common.services.discord.dto.guild.GuildChannelPosition;
import tv.v1x1.common.services.discord.dto.guild.GuildEmbed;
import tv.v1x1.common.services.discord.dto.guild.GuildMember;
import tv.v1x1.common.services.discord.dto.guild.GuildRolePosition;
import tv.v1x1.common.services.discord.dto.guild.Integration;
import tv.v1x1.common.services.discord.dto.guild.ModifyCurrentUsersNickRequest;
import tv.v1x1.common.services.discord.dto.guild.ModifyGuildIntegrationRequest;
import tv.v1x1.common.services.discord.dto.guild.ModifyGuildMemberRequest;
import tv.v1x1.common.services.discord.dto.guild.ModifyGuildRequest;
import tv.v1x1.common.services.discord.dto.guild.ModifyGuildRoleRequest;
import tv.v1x1.common.services.discord.dto.guild.Pruned;
import tv.v1x1.common.services.discord.dto.invite.InviteMetadata;
import tv.v1x1.common.services.discord.dto.permissions.Role;
import tv.v1x1.common.services.discord.dto.voice.VoiceRegion;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 9/16/2017.
 */
public class GuildsResource {
    private final WebTarget guilds;

    public GuildsResource(final WebTarget guilds) {
        this.guilds = guilds;
    }

    public Guild createGuild(final CreateGuildRequest createGuildRequest) {
        return guilds
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createGuildRequest, MediaType.APPLICATION_JSON), Guild.class);
    }

    public Guild getGuild(final String guildId) {
        return guilds.path(guildId)
                .request(DiscordApi.ACCEPT)
                .get(Guild.class);
    }

    public Guild modifyGuild(final String guildId, final ModifyGuildRequest modifyGuildRequest) {
        return guilds.path(guildId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyGuildRequest, MediaType.APPLICATION_JSON), Guild.class);
    }

    public void deleteGuild(final String guildId) {
        guilds.path(guildId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public List<Channel> getGuildChannels(final String guildId) {
        return guilds.path(guildId).path("channels")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Channel>>() {});
    }

    public Channel createGuildChannel(final String guildId, final CreateGuildChannelRequest createGuildChannelRequest) {
        return guilds.path(guildId).path("channels")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createGuildChannelRequest, MediaType.APPLICATION_JSON), Channel.class);
    }

    public void modifyGuildChannelPositions(final String guildId,
                                            final List<GuildChannelPosition> guildChannelPositions) {
        guilds.path(guildId).path("channels")
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(guildChannelPositions, MediaType.APPLICATION_JSON));
    }

    public GuildMember getGuildMember(final String guildId, final String userId) {
        return guilds.path(guildId).path("members").path(userId)
                .request(DiscordApi.ACCEPT)
                .get(GuildMember.class);
    }

    public List<GuildMember> listGuildMembers(final String guildId, final Integer limit, final String after) {
        return guilds.path(guildId).path("members")
                .queryParam("limit", limit).queryParam("after", after)
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<GuildMember>>() {});
    }

    public GuildMember addGuildMember(final String guildId, final String userId,
                                      final AddGuildMemberRequest addGuildMemberRequest) {
        return guilds.path(guildId).path("members").path(userId)
                .request(DiscordApi.ACCEPT)
                .put(Entity.entity(addGuildMemberRequest, MediaType.APPLICATION_JSON), GuildMember.class);
    }

    public void modifyGuildMember(final String guildId, final String userId,
                                  final ModifyGuildMemberRequest modifyGuildMemberRequest) {
        guilds.path(guildId).path("members").path(userId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyGuildMemberRequest, MediaType.APPLICATION_JSON));
    }

    public String modifyCurrentUsersNick(final String guildId,
                                         final ModifyCurrentUsersNickRequest modifyCurrentUsersNickRequest) {
        return guilds.path(guildId).path("members").path("@me").path("nick")
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyCurrentUsersNickRequest, MediaType.APPLICATION_JSON), String.class);
    }

    public void addGuildMemberRole(final String guildId, final String userId, final String roleId) {
        guilds.path(guildId).path("members").path(userId).path("roles").path(roleId)
                .request(DiscordApi.ACCEPT)
                .put(null);
    }

    public void removeGuildMemberRole(final String guildId, final String userId, final String roleId) {
        guilds.path(guildId).path("members").path(userId).path("roles").path(roleId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void removeGuildMember(final String guildId, final String userId) {
        guilds.path(guildId).path("members").path(userId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public List<Ban> getGuildBans(final String guildId) {
        return guilds.path(guildId).path("bans")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Ban>>() {});
    }

    public void createGuildBan(final String guildId, final String userId, final Integer deleteMessageDays,
                               final String reason) {
        guilds.path(guildId).path("bans").path(userId)
                .queryParam("delete-message-days", deleteMessageDays)
                .queryParam("reason", reason)
                .request(DiscordApi.ACCEPT)
                .put(null);
    }

    public void removeGuildBan(final String guildId, final String userId) {
        guilds.path(guildId).path("bans").path(userId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public List<Role> getGuildRoles(final String guildId) {
        return guilds.path(guildId).path("roles")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Role>>() {});
    }

    public Role createGuildRole(final String guildId, final CreateGuildRoleRequest createGuildRoleRequest) {
        return guilds.path(guildId).path("roles")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createGuildRoleRequest, MediaType.APPLICATION_JSON), Role.class);
    }

    public List<Role> modifyGuildRolePositions(final String guildId, final List<GuildRolePosition> guildRolePositions) {
        return guilds.path(guildId).path("roles")
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(guildRolePositions, MediaType.APPLICATION_JSON),new GenericType<List<Role>>() {});
    }

    public Role modifyGuildRole(final String guildId, final String roleId,
                                final ModifyGuildRoleRequest modifyGuildRoleRequest) {
        return guilds.path(guildId).path("roles").path(roleId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyGuildRoleRequest, MediaType.APPLICATION_JSON), Role.class);
    }

    public void deleteGuildRole(final String guildId, final String roleId) {
        guilds.path(guildId).path("roles").path(roleId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public Pruned getGuildPruneCount(final String guildId, final Integer days) {
        return guilds.path(guildId).path("prune")
                .queryParam("days", days)
                .request(DiscordApi.ACCEPT)
                .get(Pruned.class);
    }

    public Pruned beginGuildPrune(final String guildId, final Integer days) {
        return guilds.path(guildId).path("prune")
                .queryParam("days", days)
                .request(DiscordApi.ACCEPT)
                .post(null, Pruned.class);
    }

    public List<VoiceRegion> getGuildVoiceRegions(final String guildId) {
        return guilds.path(guildId).path("regions")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<VoiceRegion>>() {});
    }

    public List<InviteMetadata> getGuildInvites(final String guildId) {
        return guilds.path(guildId).path("invites")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<InviteMetadata>>() {});
    }

    public List<Integration> getGuildIntegrations(final String guildId) {
        return guilds.path(guildId).path("integrations")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Integration>>() {});
    }

    public void createGuildIntegration(final String guildId,
                                       final CreateGuildIntegrationRequest createGuildIntegrationRequest) {
        guilds.path(guildId).path("integrations")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createGuildIntegrationRequest, MediaType.APPLICATION_JSON));
    }

    public void modifyGuildIntegration(final String guildId, final String integrationId,
                                       final ModifyGuildIntegrationRequest modifyGuildIntegrationRequest) {
        guilds.path(guildId).path("integrations").path(integrationId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyGuildIntegrationRequest, MediaType.APPLICATION_JSON));
    }

    public void deleteGuildIntegration(final String guildId, final String integrationId) {
        guilds.path(guildId).path("integrations").path(integrationId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void syncGuildIntegration(final String guildId, final String integrationId) {
        guilds.path(guildId).path("integrations").path(integrationId).path("sync")
                .request(DiscordApi.ACCEPT)
                .post(null);
    }

    public GuildEmbed getGuildEmbed(final String guildId) {
        return guilds.path(guildId).path("embed")
                .request(DiscordApi.ACCEPT)
                .get(GuildEmbed.class);
    }

    public GuildEmbed modifyGuildEmbed(final String guildId, final GuildEmbed guildEmbed) {
        return guilds.path(guildId).path("embed")
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(guildEmbed, MediaType.APPLICATION_JSON), GuildEmbed.class);
    }
}
