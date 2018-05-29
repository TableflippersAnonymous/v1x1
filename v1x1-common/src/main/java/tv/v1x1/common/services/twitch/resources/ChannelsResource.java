package tv.v1x1.common.services.twitch.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.services.twitch.dto.channels.ChannelRequest;
import tv.v1x1.common.services.twitch.dto.channels.CommercialRequest;
import tv.v1x1.common.services.twitch.dto.channels.FollowerList;
import tv.v1x1.common.services.twitch.dto.channels.PrivateChannel;
import tv.v1x1.common.services.twitch.dto.channels.Subscriber;
import tv.v1x1.common.services.twitch.dto.channels.SubscriberList;
import tv.v1x1.common.services.twitch.dto.communities.Community;
import tv.v1x1.common.services.twitch.dto.misc.SortDirection;
import tv.v1x1.common.services.twitch.dto.channels.UpdateChannelRequest;
import tv.v1x1.common.services.twitch.dto.teams.TeamList;
import tv.v1x1.common.services.twitch.dto.users.UserList;
import tv.v1x1.common.services.twitch.dto.videos.TotalledVideoList;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Created by cobi on 10/29/2016.
 */
public class ChannelsResource {
    private final WebTarget channels;

    public ChannelsResource(final WebTarget channels) {
        this.channels = channels;
    }

    /**
     * Gets a channel object based on a specified OAuth token. Get Channel returns more data than Get Channel by ID
     * because Get Channel is privileged.
     */
    public PrivateChannel getChannel() {
        return channels.request(TwitchApi.ACCEPT).get(PrivateChannel.class);
    }

    /**
     * Gets a specified channel object.
     * @param channelId ID of channel
     */
    public Channel getChannel(final String channelId) {
        return channels.path(channelId).request(TwitchApi.ACCEPT).get(Channel.class);
    }

    /**
     * Updates specified properties of a specified channel.
     * @param channelId ID of channel
     * @param status Description of the broadcaster’s status, displayed as a title on the channel page.
     * @param game Name of game.
     * @param delay Channel delay, in seconds. This inserts a delay in the live feed. Requires the channel owner’s OAuth token.
     * @param channelFeedEnabled If true, the channel’s feed is turned on. Requires the channel owner’s OAuth token. Default: false.
     */
    public Channel updateChannel(final String channelId, final String status, final String game, final Integer delay, final Boolean channelFeedEnabled) {
        return channels.path(channelId).request(TwitchApi.ACCEPT)
                .put(Entity.entity(new UpdateChannelRequest(new ChannelRequest(status, game, delay, channelFeedEnabled)), MediaType.APPLICATION_JSON), Channel.class);
    }

    /**
     * Gets a list of users who are editors for a specified channel.
     * @param channelId ID of channel
     */
    public UserList getEditors(final String channelId) {
        return channels.path(channelId).path("editors").request(TwitchApi.ACCEPT).get(UserList.class);
    }

    /**
     * Gets a list of users who follow a specified channel, sorted by the date when they started following the channel
     * (newest first, unless specified otherwise).
     * @param channelId ID of channel
     * @param limit Maximum number of objects to return. Default: 25. Maximum: 100.
     * @param cursor Tells the server where to start fetching the next set of results, in a multi-page response.
     * @param direction Direction of sorting. Valid values: asc, desc (newest first). Default: desc.
     */
    public FollowerList getFollowers(final String channelId, final Integer limit, final String cursor, final SortDirection direction) {
        return channels.path(channelId).path("follows").queryParam("limit", limit).queryParam("cursor", cursor).queryParam("direction", direction == null ? null : direction.name())
                .request(TwitchApi.ACCEPT)
                .get(FollowerList.class);
    }

    /**
     * Gets a list of teams to which a specified channel belongs.
     * @param channelId ID of channel
     */
    public TeamList getTeams(final String channelId) {
        return channels.path(channelId).path("teams").request(TwitchApi.ACCEPT)
                .get(TeamList.class);
    }

    /**
     * Gets a list of users subscribed to a specified channel, sorted by the date when they subscribed.
     * @param channelId ID of channel
     * @param limit Maximum number of objects to return. Default: 25. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param direction Sorting direction. Valid values: asc, desc. Default: asc (oldest first).
     */
    public SubscriberList getSubscribers(final String channelId, final Integer limit, final Integer offset, final SortDirection direction) {
        return channels.path(channelId).path("subscriptions")
                .queryParam("limit", limit).queryParam("offset", offset).queryParam("direction", direction == null ? null : direction.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get(SubscriberList.class);
    }

    /**
     * Checks if a specified channel has a specified user subscribed to it. Intended for use by channel owners. Returns
     * a subscription object which includes the user if that user is subscribed. Requires authentication for the channel.
     * @param channelId ID of channel
     * @param userId ID of user
     */
    public Subscriber getSubscriber(final String channelId, final String userId) {
        return channels.path(channelId).path("subscriptions").path(userId)
                .request(TwitchApi.ACCEPT)
                .get(Subscriber.class);
    }

    /**
     * Gets a list of videos from a specified channel.
     * @param channelId ID of channel
     * @param limit Maximum number of objects to return. Default: 10. Maximum: 100.
     * @param offset Object offset for pagination of results. Default: 0.
     * @param broadcastType Constrains the type of videos returned. Valid values: (any combination of) archive,
     *                      highlight, upload, Default: highlight.
     * @param language Constrains the language of the videos that are returned; for example, en,es. Default: all
     *                 languages.
     * @param sort Sorting order of the returned objects. Valid values: views, time. Default: time (most recent first).
     */
    public TotalledVideoList getVideos(final String channelId, final Integer limit, final Integer offset, final Set<String> broadcastType, final Set<String> language, final String sort) {
        return channels.path(channelId).path("videos")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("broadcast_type", Joiner.on(",").join(broadcastType))
                .queryParam("language", Joiner.on(",").join(language))
                .queryParam("sort", sort)
                .request(TwitchApi.ACCEPT)
                .get(TotalledVideoList.class);
    }

    /**
     * Starts a commercial (advertisement) on a specified channel. This is valid only for channels that are Twitch
     * partners. You cannot start a commercial more often than once every 8 minutes.
     * @param channelId ID of channel
     * @param length The length of the commercial (in seconds) is specified in the request body, with a required
     *               duration parameter. Valid values are 30, 60, 90, 120, 150, and 180.
     */
    public void runCommercial(final String channelId, final int length) {
        channels.path(channelId).path("commercial").request(TwitchApi.ACCEPT)
                .post(Entity.entity(new CommercialRequest(length), MediaType.APPLICATION_JSON));
    }

    /**
     * Deletes the stream key for a specified channel. Once it is deleted, the stream key is automatically reset. A
     * stream key (also known as authorization key) uniquely identifies a stream. Each broadcast uses an RTMP URL that
     * includes the stream key. Stream keys are assigned by Twitch.
     * @param channelId ID of channel
     */
    public PrivateChannel resetStreamKey(final String channelId) {
        return channels.path(channelId).path("stream_key").request(TwitchApi.ACCEPT)
                .delete(PrivateChannel.class);
    }

    /**
     * Gets the community for a specified channel.
     * @param channelId ID of channel
     */
    public Community getCommunity(final String channelId) {
        return channels.path(channelId).path("community").request(TwitchApi.ACCEPT)
                .get(Community.class);
    }

    /**
     * Sets a specified channel to be in a specified community.
     * @param channelId ID of channel
     * @param communityId ID of community
     */
    public void setCommunity(final String channelId, final String communityId) {
        channels.path(channelId).path("community").path(communityId).request(TwitchApi.ACCEPT)
                .put(null);
    }

    /**
     * Deletes a specified channel from its community.
     * @param channelId ID of channel
     */
    public void removeCommunity(final String channelId) {
        channels.path(channelId).path("community").request(TwitchApi.ACCEPT)
                .delete();
    }
}
