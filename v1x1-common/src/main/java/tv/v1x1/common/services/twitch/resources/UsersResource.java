package tv.v1x1.common.services.twitch.resources;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.blocks.Block;
import tv.v1x1.common.services.twitch.dto.blocks.BlockList;
import tv.v1x1.common.services.twitch.dto.emotes.EmoticonSetList;
import tv.v1x1.common.services.twitch.dto.misc.SortDirection;
import tv.v1x1.common.services.twitch.dto.users.Follow;
import tv.v1x1.common.services.twitch.dto.users.FollowList;
import tv.v1x1.common.services.twitch.dto.users.FollowRequest;
import tv.v1x1.common.services.twitch.dto.users.PrivateUser;
import tv.v1x1.common.services.twitch.dto.users.Subscription;
import tv.v1x1.common.services.twitch.dto.users.User;
import tv.v1x1.common.services.twitch.dto.users.UserList;
import tv.v1x1.common.services.twitch.dto.users.VhsId;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by cobi on 10/30/2016.
 */
public class UsersResource {
    private final WebTarget users;
    private final WebTarget user;

    public UsersResource(final WebTarget users, final WebTarget user) {
        this.users = users;
        this.user = user;
    }

    /**
     * Gets a user object based on the OAuth token provided.
     * If the user’s Twitch-registered email address is not verified, null is returned.
     * Get User returns more data than Get User by ID, because Get User is privileged.
     */
    public PrivateUser getUser() {
        return user
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(PrivateUser.class);
    }

    /**
     * To translate from a V3 user name to a V5 user ID, use the Get Users endpoint with up to 100 logins
     * @param usernames Up to 100 usernames
     */
    public UserList getUsersByUsernames(final List<String> usernames) {
        return users
                .queryParam("login", Joiner.on(",").join(usernames))
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(UserList.class);
    }

    /**
     * Get a specific user by username
     * @param username Name of user
     */
    public User getUserByUsername(final String username) {
        final List<User> userList = getUsersByUsernames(ImmutableList.of(username)).getUsers();
        if(userList.size() == 0)
            return null;
        return userList.get(0);
    }

    /**
     * Gets a specified user object.
     * @param userId ID of user
     */
    public User getUser(final String userId) {
        return users.path(userId)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(User.class);
    }

    /**
     * Gets a list of the emojis and emoticons that the specified user can use in chat. These are both the globally
     * available ones and the channel-specific ones (which can be accessed by any user subscribed to the channel).
     * @param userId ID of user
     */
    public EmoticonSetList getEmotes(final String userId) {
        return users.path(userId).path("emotes")
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(EmoticonSetList.class);
    }

    /**
     * Checks if a specified user is subscribed to a specified channel.
     * @param userId ID of user
     * @param channelId ID of channel
     */
    public Subscription getSubscription(final String userId, final String channelId) {
        return users.path(userId).path("subscriptions").path(channelId)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Subscription.class);
    }

    /**
     * Gets a list of all channels followed by a specified user, sorted by the date when they started following each channel.
     * @param userId ID of user
     * @param limit Maximum number of most-recent objects to return. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param direction Sorting direction. Valid values: asc, desc. Default: desc (newest first).
     * @param sortBy Sorting key. Valid values: created_at, last_broadcast, login. Default: created_at.
     */
    public FollowList getFollows(final String userId, final Integer limit, final Integer offset, final SortDirection direction, final String sortBy) {
        return users.path(userId).path("follows").path("channels")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("direction", direction == null ? null : direction.name()).queryParam("sortby", sortBy)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(FollowList.class);
    }

    /**
     * Checks if a specified user follows a specified channel. If the user is following the channel, a follow object is returned.
     * @param userId ID of user
     * @param channelId ID of channel
     */
    public Follow getFollow(final String userId, final String channelId) {
        return users.path(userId).path("follows").path("channels").path(channelId)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Follow.class);
    }

    /**
     * Adds a specified user to the followers of a specified channel.
     * @param userId ID of user
     * @param channelId ID of channel
     * @param notifications If true, the user gets email or push notifications (depending on his notification settings)
     *                      when the channel goes live. Default: false.
     */
    public Follow follow(final String userId, final String channelId, final boolean notifications) {
        return users.path(userId).path("follows").path("channels").path(channelId)
                .request(TwitchApi.ACCEPT)
                .put(Entity.entity(new FollowRequest(notifications), MediaType.APPLICATION_JSON))
                .readEntity(Follow.class);
    }

    /**
     * Deletes a specified user from the followers of a specified channel.
     * @param userId ID of user
     * @param channelId ID of channel
     */
    public void unfollow(final String userId, final String channelId) {
        users.path(userId).path("follows").path("channels").path(channelId)
                .request(TwitchApi.ACCEPT)
                .delete();
    }

    /**
     * Gets a user’s block list. List sorted by recency, newest first.
     * @param userId ID of user
     * @param limit Maximum number of objects in array. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination. Default: 0.
     */
    public BlockList getBlocks(final String userId, final int limit, final int offset) {
        return users.path(userId).path("blocks")
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(BlockList.class);
    }

    /**
     * Blocks a user; that is, adds a specified target user to the blocks list of a specified source user.
     * @param userId ID of user (source)
     * @param targetId ID of target user
     */
    public Block block(final String userId, final String targetId) {
        return users.path(userId).path("blocks").path(targetId).request(TwitchApi.ACCEPT).put(null).readEntity(Block.class);
    }

    /**
     * Unblocks a user; that is, deletes a specified target user from the blocks list of a specified source user.
     * @param userId ID of user (source)
     * @param targetId ID of target user
     */
    public void unblock(final String userId, final String targetId) {
        users.path(userId).path("blocks").path(targetId).request(TwitchApi.ACCEPT).delete();
    }

    /**
     * Creates a connection between a user (an authenticated Twitch user, linked to a game user) and VHS (Viewer
     * Heartbeat Service), and starts returning the user’s VHS data in each heartbeat. The game user is specified by a
     * required identifier parameter.
     * @param identifier The game user
     */
    public void connectVHS(final String identifier) {
        user.path("vhs")
                .request(TwitchApi.ACCEPT)
                .put(Entity.entity(new VhsId(identifier), MediaType.APPLICATION_JSON));
    }

    /**
     * Checks whether an authenticated Twitch user is connected to VHS.
     * If a connection to the service exists for the specified user, the linked game user’s ID is returned; otherwise,
     * an HTTP 404 response is returned.
     */
    public VhsId getVHS() {
        return user.path("vhs")
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(VhsId.class);
    }

    /**
     * Deletes the connection between an authenticated Twitch user and VHS.
     */
    public void disconnectVHS() {
        user.path("vhs")
                .request(TwitchApi.ACCEPT)
                .delete();
    }
}
