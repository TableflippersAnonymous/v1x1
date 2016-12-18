package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.channels.Channel;
import tv.v1x1.common.services.twitch.dto.channels.ChannelRequest;
import tv.v1x1.common.services.twitch.dto.channels.CommercialRequest;
import tv.v1x1.common.services.twitch.dto.channels.PrivateChannel;
import tv.v1x1.common.services.twitch.dto.teams.Team;
import tv.v1x1.common.services.twitch.dto.channels.UpdateChannelRequest;
import tv.v1x1.common.services.twitch.dto.users.UserList;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Created by cobi on 10/29/2016.
 */
public class ChannelsResource {
    private final WebTarget channels;

    public ChannelsResource(final WebTarget channels) {
        this.channels = channels;
    }

    public Channel getChannel(final String channel) {
        return channels.path(channel).request(TwitchApi.ACCEPT).get(Channel.class);
    }

    public PrivateChannel getChannel() {
        return channels.request(TwitchApi.ACCEPT).get().readEntity(PrivateChannel.class);
    }

    public UserList getEditors(final String channel) {
        return channels.path(channel).path("editors").request(TwitchApi.ACCEPT).get().readEntity(UserList.class);
    }

    public Channel updateChannel(final String channel, final String status, final String game, final Integer delay, final Boolean channelFeedEnabled) {
        return channels.path(channel).request(TwitchApi.ACCEPT)
                .put(Entity.entity(new UpdateChannelRequest(new ChannelRequest(status, game, delay, channelFeedEnabled)), MediaType.APPLICATION_JSON))
                .readEntity(Channel.class);
    }

    public PrivateChannel resetStreamKey(final String channel) {
        return channels.path(channel).path("stream_key").request(TwitchApi.ACCEPT)
                .delete()
                .readEntity(PrivateChannel.class);
    }

    public void runCommercial(final String channel, final int length) {
        channels.path(channel).path("commercial").request(TwitchApi.ACCEPT)
                .post(Entity.entity(new CommercialRequest(length), MediaType.APPLICATION_JSON));
    }

    public Team getTeam(final String channel) {
        return channels.path(channel).path("teams").request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Team.class);
    }
}
