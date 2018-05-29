package tv.v1x1.common.services.discord.resources;

import tv.v1x1.common.services.discord.DiscordApi;
import tv.v1x1.common.services.discord.dto.channel.BulkDeleteMessagesRequest;
import tv.v1x1.common.services.discord.dto.channel.Channel;
import tv.v1x1.common.services.discord.dto.channel.CreateChannelInviteRequest;
import tv.v1x1.common.services.discord.dto.channel.CreateMessageRequest;
import tv.v1x1.common.services.discord.dto.channel.EditChannelPermissionsRequest;
import tv.v1x1.common.services.discord.dto.channel.EditMessageRequest;
import tv.v1x1.common.services.discord.dto.channel.GroupDmAddRecipientRequest;
import tv.v1x1.common.services.discord.dto.channel.Message;
import tv.v1x1.common.services.discord.dto.channel.ModifyChannelRequest;
import tv.v1x1.common.services.discord.dto.invite.Invite;
import tv.v1x1.common.services.discord.dto.invite.InviteMetadata;
import tv.v1x1.common.services.discord.dto.user.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
public class ChannelsResource {
    private final WebTarget channels;

    public ChannelsResource(final WebTarget channels) {
        this.channels = channels;
    }

    public Channel getChannel(final String channelId) {
        return channels.path(channelId).request(DiscordApi.ACCEPT).get(Channel.class);
    }

    public Channel modifyChannel(final String channelId, final ModifyChannelRequest modifyChannelRequest) {
        return channels.path(channelId).request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(modifyChannelRequest, MediaType.APPLICATION_JSON), Channel.class);
    }

    public Channel deleteChannel(final String channelId) {
        return channels.path(channelId).request(DiscordApi.ACCEPT)
                .delete(Channel.class);
    }

    public List<Message> getChannelMessages(final String channelId, final String around, final String before,
                                            final String after, final Integer limit) {
        return channels.path(channelId).path("messages")
                .queryParam("around", around).queryParam("before", before)
                .queryParam("after", after).queryParam("limit", limit)
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Message>>() {});
    }

    public Message getChannelMessage(final String channelId, final String messageId) {
        return channels.path(channelId).path("messages").path(messageId)
                .request(DiscordApi.ACCEPT)
                .get(Message.class);
    }

    public Message createMessage(final String channelId, final CreateMessageRequest createMessageRequest) {
        return channels.path(channelId).path("messages")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createMessageRequest, MediaType.APPLICATION_JSON), Message.class);
    }

    public void createReaction(final String channelId, final String messageId, final String emoji) {
        channels.path(channelId).path("messages").path(messageId).path("reactions").path(emoji).path("@me")
                .request(DiscordApi.ACCEPT)
                .put(null);
    }

    public void deleteOwnReaction(final String channelId, final String messageId, final String emoji) {
        channels.path(channelId).path("messages").path(messageId).path("reactions").path(emoji).path("@me")
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void deleteUserReaction(final String channelId, final String messageId, final String emoji,
                                   final String userId) {
        channels.path(channelId).path("messages").path(messageId).path("reactions").path(emoji).path(userId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public List<User> getReactions(final String channelId, final String messageId, final String emoji) {
        return channels.path(channelId).path("messages").path(messageId).path("reactions").path(emoji)
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<User>>() {});
    }

    public void deleteAllReactions(final String channelId, final String messageId) {
        channels.path(channelId).path("messages").path(messageId).path("reactions")
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public Message editMessage(final String channelId, final String messageId,
                               final EditMessageRequest editMessageRequest) {
        return channels.path(channelId).path("messages").path(messageId)
                .request(DiscordApi.ACCEPT)
                .method("PATCH", Entity.entity(editMessageRequest, MediaType.APPLICATION_JSON), Message.class);
    }

    public void deleteMessage(final String channelId, final String messageId) {
        channels.path(channelId).path("messages").path(messageId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void bulkDeleteMessages(final String channelId, final BulkDeleteMessagesRequest bulkDeleteMessagesRequest) {
        channels.path(channelId).path("messages").path("bulk-delete")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(bulkDeleteMessagesRequest, MediaType.APPLICATION_JSON));
    }

    public void editChannelPermissions(final String channelId, final String overwriteId,
                                       final EditChannelPermissionsRequest editChannelPermissionsRequest) {
        channels.path(channelId).path("permissions").path(overwriteId)
                .request(DiscordApi.ACCEPT)
                .put(Entity.entity(editChannelPermissionsRequest, MediaType.APPLICATION_JSON));
    }

    public List<InviteMetadata> getChannelInvites(final String channelId) {
        return channels.path(channelId).path("invites")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<InviteMetadata>>() {});
    }

    public Invite createChannelInvite(final String channelId, final CreateChannelInviteRequest createChannelInviteRequest) {
        return channels.path(channelId).path("invites")
                .request(DiscordApi.ACCEPT)
                .post(Entity.entity(createChannelInviteRequest, MediaType.APPLICATION_JSON), Invite.class);
    }

    public void deleteChannelPermission(final String channelId, final String overwriteId) {
        channels.path(channelId).path("permissions").path(overwriteId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void triggerTypingIndicator(final String channelId) {
        channels.path(channelId).path("typing")
                .request(DiscordApi.ACCEPT)
                .post(null);
    }

    public List<Message> getPinnedMessages(final String channelId) {
        return channels.path(channelId).path("pins")
                .request(DiscordApi.ACCEPT)
                .get(new GenericType<List<Message>>() {});
    }

    public void addPinnedChannelMessage(final String channelId, final String messageId) {
        channels.path(channelId).path("pins").path(messageId)
                .request(DiscordApi.ACCEPT)
                .put(null);
    }

    public void deletePinnedChannelMessage(final String channelId, final String messageId) {
        channels.path(channelId).path("pins").path(messageId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }

    public void groupDmAddRecipient(final String channelId, final String userId,
                                    final GroupDmAddRecipientRequest groupDmAddRecipientRequest) {
        channels.path(channelId).path("recipients").path(userId)
                .request(DiscordApi.ACCEPT)
                .put(Entity.entity(groupDmAddRecipientRequest, MediaType.APPLICATION_JSON));
    }

    public void groupDmRemoveRecipient(final String channelId, final String userId) {
        channels.path(channelId).path("recipients").path(userId)
                .request(DiscordApi.ACCEPT)
                .delete();
    }
}
