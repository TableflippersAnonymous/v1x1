package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChatMessageOuterClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a single chat message from a {@link User} that the bot received
 * @author Cobi
 */
public class ChatMessage {
    public static ChatMessage fromProto(final ChatMessageOuterClass.ChatMessage message) {
        final Channel channel = Channel.fromProto(message.getChannel());
        final User sender = User.fromProto(message.getSender());
        final String text = message.getText();
        final List<Permission> permissions = message.getPermissionsList().stream().map(Permission::fromProto).collect(Collectors.toList());
        return new ChatMessage(channel, sender, text, permissions);
    }

    private final Channel channel;
    private final User sender;
    private final String text;
    private final List<Permission> permissions;

    public ChatMessage(final Channel channel, final User sender, final String text, final List<Permission> permissions) {
        this.channel = channel;
        this.sender = sender;
        this.text = text;
        this.permissions = permissions;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public ChatMessageOuterClass.ChatMessage toProto() {
        return ChatMessageOuterClass.ChatMessage.newBuilder()
                .setChannel(channel.toProto())
                .setSender(sender.toProto())
                .setText(text)
                .addAllPermissions(permissions.stream().map(Permission::toProto).collect(Collectors.toList()))
                .build();
    }
}
