package tv.v1x1.modules.core.api.api.pubsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.modules.core.api.api.rest.Channel;
import tv.v1x1.modules.core.api.api.rest.User;

import java.util.List;
import java.util.UUID;

/**
 * Created by cobi on 7/10/2017.
 */
public class ChatMessagePubSub {
    @JsonProperty("message_id")
    private UUID messageId;
    @JsonProperty("originating_module")
    private String originatingModule;
    @JsonProperty
    private Channel channel;
    @JsonProperty
    private User user;
    @JsonProperty
    private String text;
    @JsonProperty
    private List<String> permissions;

    public ChatMessagePubSub() {
    }

    public ChatMessagePubSub(final UUID messageId, final String originatingModule, final Channel channel, final User user, final String text, final List<String> permissions) {
        this.messageId = messageId;
        this.originatingModule = originatingModule;
        this.channel = channel;
        this.user = user;
        this.text = text;
        this.permissions = permissions;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(final UUID messageId) {
        this.messageId = messageId;
    }

    public String getOriginatingModule() {
        return originatingModule;
    }

    public void setOriginatingModule(final String originatingModule) {
        this.originatingModule = originatingModule;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = permissions;
    }
}
