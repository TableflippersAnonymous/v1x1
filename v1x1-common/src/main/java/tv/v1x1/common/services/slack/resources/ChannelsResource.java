package tv.v1x1.common.services.slack.resources;

import tv.v1x1.common.services.slack.SlackApi;
import tv.v1x1.common.services.slack.dto.channels.ChannelHistoryResponse;
import tv.v1x1.common.services.slack.exceptions.ChannelNotFoundException;
import tv.v1x1.common.services.slack.exceptions.SlackApiException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import java.util.Objects;

public class ChannelsResource {
    private final WebTarget api;
    private final String token;

    public ChannelsResource(final WebTarget api, final String token) {
        this.api = api;
        this.token = token;
    }

    public ChannelHistoryResponse getChannelHistory(final String channelId, final Integer count, final Boolean inclusive,
                                                    final String latest, final String oldest, final Boolean unreads) throws ChannelNotFoundException {
        final ChannelHistoryResponse channelHistoryResponse = this.api.path("channels.history")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("channel", channelId)
                        .param("count", count == null ? null : count.toString())
                        .param("inclusive", inclusive == null ? null : inclusive.toString())
                        .param("latest", latest)
                        .param("oldest", oldest)
                        .param("unreads", unreads == null ? null : unreads.toString())
                ))
                .readEntity(ChannelHistoryResponse.class);
        if(channelHistoryResponse.isOk())
            return channelHistoryResponse;
        if(Objects.equals(channelHistoryResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(channelHistoryResponse.getError());
    }

    public ChannelInfoResponse getChannelInfo(final String channelId, )
}
