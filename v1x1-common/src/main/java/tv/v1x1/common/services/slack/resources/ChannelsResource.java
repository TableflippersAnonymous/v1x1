package tv.v1x1.common.services.slack.resources;

import tv.v1x1.common.services.slack.SlackApi;
import tv.v1x1.common.services.slack.dto.channels.ChannelHistoryResponse;
import tv.v1x1.common.services.slack.dto.channels.ChannelInfoResponse;
import tv.v1x1.common.services.slack.dto.channels.ChannelListResponse;
import tv.v1x1.common.services.slack.dto.channels.ChannelRepliesResponse;
import tv.v1x1.common.services.slack.exceptions.ChannelNotFoundException;
import tv.v1x1.common.services.slack.exceptions.SlackApiException;
import tv.v1x1.common.services.slack.exceptions.ThreadNotFoundException;

import javax.annotation.Nullable;
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

    public ChannelHistoryResponse getChannelHistory(final String channelId, @Nullable final Integer count,
                                                    @Nullable final Boolean inclusive, @Nullable final String latest,
                                                    @Nullable final String oldest, @Nullable final Boolean unreads)
            throws ChannelNotFoundException {
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

    public ChannelInfoResponse getChannelInfo(final String channelId, @Nullable final Boolean includeLocale)
            throws ChannelNotFoundException {
        final ChannelInfoResponse channelInfoResponse = this.api.path("channels.info")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("channel", channelId)
                        .param("include_locale", includeLocale == null ? null : includeLocale.toString())
                ))
                .readEntity(ChannelInfoResponse.class);
        if(channelInfoResponse.isOk())
            return channelInfoResponse;
        if(Objects.equals(channelInfoResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(channelInfoResponse.getError());
    }

    public ChannelListResponse getChannelList(final String cursor, @Nullable final Boolean excludeArchived,
                                              @Nullable final Boolean excludeMembers, @Nullable final Integer limit) {
        final ChannelListResponse channelListResponse = this.api.path("channels.list")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("cursor", cursor)
                        .param("exclude_archived", excludeArchived == null ? null : excludeArchived.toString())
                        .param("exclude_members", excludeMembers == null ? null : excludeMembers.toString())
                        .param("limit", limit == null ? null : limit.toString())
                ))
                .readEntity(ChannelListResponse.class);
        if(channelListResponse.isOk())
            return channelListResponse;
        throw new SlackApiException(channelListResponse.getError());
    }

    public ChannelRepliesResponse getChannelReplies(final String channelId, final String threadTs)
            throws ChannelNotFoundException, ThreadNotFoundException {
        final ChannelRepliesResponse channelRepliesResponse = this.api.path("channels.replies")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("channel", channelId)
                        .param("thread_ts", threadTs)
                ))
                .readEntity(ChannelRepliesResponse.class);
        if(channelRepliesResponse.isOk())
            return channelRepliesResponse;
        if(Objects.equals(channelRepliesResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        if(Objects.equals(channelRepliesResponse.getError(), "thread_not_found"))
            throw new ThreadNotFoundException();
        throw new SlackApiException(channelRepliesResponse.getError());
    }
}
