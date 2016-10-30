package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.users.Follow;
import tv.v1x1.common.services.twitch.dto.users.FollowList;
import tv.v1x1.common.services.twitch.dto.channels.FollowerList;
import tv.v1x1.common.services.twitch.dto.misc.SortDirection;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/29/2016.
 */
public class FollowsResource {
    private final WebTarget channels;
    private final WebTarget users;

    public FollowsResource(final WebTarget channels, final WebTarget users) {
        this.channels = channels;
        this.users = users;
    }

    public FollowerList getFollowers(final String channel, final Integer limit, final String cursor, final SortDirection direction) {
        return channels.path(channel).path("follows").queryParam("limit", limit).queryParam("cursor", cursor).queryParam("direction", direction == null ? null : direction.name())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(FollowerList.class);
    }

    public FollowList getFollows(final String user, final Integer limit, final Integer offset, final SortDirection direction, final String sortBy) {
        return users.path(user).path("follows").path("channels")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("direction", direction == null ? null : direction.name()).queryParam("sortby", sortBy)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(FollowList.class);
    }

    public Follow getFollow(final String user, final String channel) {
        return users.path(user).path("follows").path("channels").path(channel)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Follow.class);
    }

    public Follow follow(final String user, final String channel, final boolean notifications) {
        return users.path(user).path("follows").path("channels").path(channel)
                .queryParam("notifications", notifications)
                .request(TwitchApi.ACCEPT)
                .put(null)
                .readEntity(Follow.class);
    }

    public void unfollow(final String user, final String channel) {
        users.path(user).path("follows").path("channels").path(channel)
                .request(TwitchApi.ACCEPT)
                .delete();
    }
}
