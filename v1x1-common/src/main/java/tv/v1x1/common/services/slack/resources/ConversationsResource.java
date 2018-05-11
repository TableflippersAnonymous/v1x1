package tv.v1x1.common.services.slack.resources;

import tv.v1x1.common.services.slack.SlackApi;
import tv.v1x1.common.services.slack.dto.conversations.ConversationCloseRequest;
import tv.v1x1.common.services.slack.dto.conversations.ConversationCloseResponse;
import tv.v1x1.common.services.slack.dto.conversations.ConversationHistoryResponse;
import tv.v1x1.common.services.slack.dto.conversations.ConversationInfoResponse;
import tv.v1x1.common.services.slack.exceptions.ChannelNotFoundException;
import tv.v1x1.common.services.slack.exceptions.SlackApiException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

public class ConversationsResource {
    private final WebTarget api;
    private final String token;

    public ConversationsResource(final WebTarget api, final String token) {
        this.api = api;
        this.token = token;
    }

    public ConversationCloseResponse closeConversation(final String channelId) throws ChannelNotFoundException {
        final ConversationCloseResponse conversationCloseResponse = this.api.path("conversations.close")
                .request(SlackApi.ACCEPT)
                .post(Entity.entity(new ConversationCloseRequest(channelId), MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ConversationCloseResponse.class);
        if(conversationCloseResponse.isOk())
            return conversationCloseResponse;
        if(Objects.equals(conversationCloseResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(conversationCloseResponse.getError());
    }

    public ConversationHistoryResponse getHistory(final String channelId, final String cursor, final Boolean inclusive,
                                                  final String latest, final Integer limit, final String oldest)
            throws ChannelNotFoundException {
        final ConversationHistoryResponse conversationHistoryResponse = this.api.path("conversations.history")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("channel", channelId)
                        .param("cursor", cursor)
                        .param("inclusive", inclusive == null ? null : inclusive.toString())
                        .param("latest", latest)
                        .param("limit", limit == null ? null : limit.toString())
                        .param("oldest", oldest)
                ))
                .readEntity(ConversationHistoryResponse.class);
        if(conversationHistoryResponse.isOk())
            return conversationHistoryResponse;
        if(Objects.equals(conversationHistoryResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(conversationHistoryResponse.getError());
    }

    public ConversationInfoResponse getInfo(final String channelId, final Boolean includeLocale) throws ChannelNotFoundException {
        final ConversationInfoResponse conversationInfoResponse = this.api.path("conversations.info")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("channel", channelId)
                        .param("include_locale", includeLocale == null ? null : includeLocale.toString())
                ))
                .readEntity(ConversationInfoResponse.class);
        if(conversationInfoResponse.isOk())
            return conversationInfoResponse;
        if(Objects.equals(conversationInfoResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(conversationInfoResponse.getError());
    }
}
