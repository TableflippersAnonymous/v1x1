package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.streams.BroadcastType;
import tv.v1x1.common.services.twitch.dto.emotes.EmoticonSetList;
import tv.v1x1.common.services.twitch.dto.users.PrivateUser;
import tv.v1x1.common.services.twitch.dto.streams.StreamList;
import tv.v1x1.common.services.twitch.dto.streams.StreamType;
import tv.v1x1.common.services.twitch.dto.users.User;
import tv.v1x1.common.services.twitch.dto.videos.VideoList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by cobi on 10/30/2016.
 */
public class UsersResource {
    private final WebTarget users;
    private final WebTarget user;
    private final WebTarget streams;
    private final WebTarget videos;

    public UsersResource(final WebTarget users, final WebTarget user, final WebTarget streams, final WebTarget videos) {
        this.users = users;
        this.user = user;
        this.streams = streams;
        this.videos = videos;
    }

    public User getUser(final String username) {
        return users.path(username)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(User.class);
    }

    public EmoticonSetList getEmotes(final String username) {
        return users.path(username).path("emotes")
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(EmoticonSetList.class);
    }

    public PrivateUser getUser() {
        return user
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(PrivateUser.class);
    }

    public StreamList getFollowedStreams(final Integer limit, final Integer offset, final StreamType streamType) {
        return streams.path("followed")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("stream_type", streamType == null ? null : streamType.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(StreamList.class);
    }

    public VideoList getFollowedVideos(final Integer limit, final Integer offset, final BroadcastType broadcastType) {
        return videos.path("followed")
                .queryParam("limit", limit).queryParam("offset", offset)
                .queryParam("broadcast_type", broadcastType == null ? null : broadcastType.name().toLowerCase())
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(VideoList.class);
    }
}
