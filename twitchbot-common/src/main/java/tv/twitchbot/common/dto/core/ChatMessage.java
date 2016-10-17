package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChatMessageOuterClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a single chat message from a {@link User} that the bot received
 * @author Cobi
 */
public class ChatMessage {
    public static ChatMessage fromProto(ChatMessageOuterClass.ChatMessage message) {
        Channel channel = Channel.fromProto(message.getChannel());
        User sender = User.fromProto(message.getSender());
        String text = message.getText();
        List<Permission> permissions = message.getPermissionsList().stream().map(Permission::fromProto).collect(Collectors.toList());
        return new ChatMessage(channel, sender, text, permissions);
    }

    private Channel channel;
    private User sender;
    private String text;
    private List<Permission> permissions;

    public ChatMessage(Channel channel, User sender, String text, List<Permission> permissions) {
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
