package tv.v1x1.common.services.slack.resources;

import tv.v1x1.common.services.slack.SlackApi;
import tv.v1x1.common.services.slack.dto.chat.ChatDeleteRequest;
import tv.v1x1.common.services.slack.dto.chat.ChatDeleteResponse;
import tv.v1x1.common.services.slack.dto.chat.ChatEphemeralRequest;
import tv.v1x1.common.services.slack.dto.chat.ChatEphemeralResponse;
import tv.v1x1.common.services.slack.dto.chat.ChatMeMessageRequest;
import tv.v1x1.common.services.slack.dto.chat.ChatMeMessageResponse;
import tv.v1x1.common.services.slack.dto.chat.ChatMessageRequest;
import tv.v1x1.common.services.slack.dto.chat.ChatMessageResponse;
import tv.v1x1.common.services.slack.dto.chat.ChatPermalinkResponse;
import tv.v1x1.common.services.slack.dto.chat.ChatUpdateRequest;
import tv.v1x1.common.services.slack.dto.chat.ChatUpdateResponse;
import tv.v1x1.common.services.slack.exceptions.ChannelNotFoundException;
import tv.v1x1.common.services.slack.exceptions.MessageNotFoundException;
import tv.v1x1.common.services.slack.exceptions.NotInChannelException;
import tv.v1x1.common.services.slack.exceptions.SlackApiException;

import javax.annotation.Nullable;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.util.Objects;

public class ChatResource {
    private final WebTarget api;
    private final String token;

    public ChatResource(final WebTarget api, final String token) {
        this.api = api;
        this.token = token;
    }

    public ChatDeleteResponse deleteMessage(final String channelId, final String messageTs,
                                            @Nullable final Boolean asUser)
            throws MessageNotFoundException, ChannelNotFoundException {
        final ChatDeleteResponse chatDeleteResponse = this.api.path("chat.delete")
                .request(SlackApi.ACCEPT)
                .post(Entity.entity(new ChatDeleteRequest(channelId, messageTs, asUser), MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ChatDeleteResponse.class);
        if(chatDeleteResponse.isOk())
            return chatDeleteResponse;
        if(Objects.equals(chatDeleteResponse.getError(), "message_not_found"))
            throw new MessageNotFoundException();
        if(Objects.equals(chatDeleteResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(chatDeleteResponse.getError());
    }

    public ChatPermalinkResponse getPermalink(final String channelId, final String messageTs)
            throws MessageNotFoundException, ChannelNotFoundException {
        final ChatPermalinkResponse chatPermalinkResponse = this.api.path("chat.getPermalink")
                .request(SlackApi.ACCEPT)
                .post(Entity.form(new Form()
                        .param("token", token)
                        .param("channel", channelId)
                        .param("message_ts", messageTs)
                ))
                .readEntity(ChatPermalinkResponse.class);
        if(chatPermalinkResponse.isOk())
            return chatPermalinkResponse;
        if(Objects.equals(chatPermalinkResponse.getError(), "message_not_found"))
            throw new MessageNotFoundException();
        if(Objects.equals(chatPermalinkResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(chatPermalinkResponse.getError());
    }

    public ChatMeMessageResponse sendMeMessage(final String channelId, final String text)
            throws NotInChannelException, ChannelNotFoundException {
        final ChatMeMessageResponse chatMeMessageResponse = this.api.path("chat.meMessage")
                .request(SlackApi.ACCEPT)
                .post(Entity.entity(new ChatMeMessageRequest(channelId, text), MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ChatMeMessageResponse.class);
        if(chatMeMessageResponse.isOk())
            return chatMeMessageResponse;
        if(Objects.equals(chatMeMessageResponse.getError(), "not_in_channel"))
            throw new NotInChannelException();
        if(Objects.equals(chatMeMessageResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(chatMeMessageResponse.getError());
    }

    public ChatEphemeralResponse sendEphemeralMessage(final ChatEphemeralRequest chatEphemeralRequest) throws ChannelNotFoundException {
        final ChatEphemeralResponse chatEphemeralResponse = this.api.path("chat.postEphemeral")
                .request(SlackApi.ACCEPT)
                .post(Entity.entity(chatEphemeralRequest, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ChatEphemeralResponse.class);
        if(chatEphemeralResponse.isOk())
            return chatEphemeralResponse;
        if(Objects.equals(chatEphemeralResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(chatEphemeralResponse.getError());
    }

    public ChatMessageResponse sendMessage(final ChatMessageRequest chatMessageRequest) throws NotInChannelException, ChannelNotFoundException {
        final ChatMessageResponse chatMessageResponse = this.api.path("chat.postMessage")
                .request(SlackApi.ACCEPT)
                .post(Entity.entity(chatMessageRequest, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ChatMessageResponse.class);
        if(chatMessageResponse.isOk())
            return chatMessageResponse;
        if(Objects.equals(chatMessageResponse.getError(), "not_in_channel"))
            throw new NotInChannelException();
        if(Objects.equals(chatMessageResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(chatMessageResponse.getError());
    }

    public ChatUpdateResponse updateMessage(final ChatUpdateRequest chatUpdateRequest) throws MessageNotFoundException, ChannelNotFoundException {
        final ChatUpdateResponse chatUpdateResponse = this.api.path("chat.update")
                .request(SlackApi.ACCEPT)
                .post(Entity.entity(chatUpdateRequest, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(ChatUpdateResponse.class);
        if(chatUpdateResponse.isOk())
            return chatUpdateResponse;
        if(Objects.equals(chatUpdateResponse.getError(), "message_not_found"))
            throw new MessageNotFoundException();
        if(Objects.equals(chatUpdateResponse.getError(), "channel_not_found"))
            throw new ChannelNotFoundException();
        throw new SlackApiException(chatUpdateResponse.getError());
    }
}
