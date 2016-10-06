package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.ChatMessageOuterClass;

/**
 * Created by cobi on 10/5/16.
 */
public class ChatMessage {
    public static ChatMessage fromProto(ChatMessageOuterClass.ChatMessage message) {
        Channel channel = Channel.fromProto(message.getChannel());
        User sender = User.fromProto(message.getSender());
        String text = message.getText();
        return new ChatMessage(channel, sender, text);
    }

    private Channel channel;
    private User sender;
    private String text;

    public ChatMessage(Channel channel, User sender, String text) {
        this.channel = channel;
        this.sender = sender;
        this.text = text;
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

    public ChatMessageOuterClass.ChatMessage toProto() {
        return ChatMessageOuterClass.ChatMessage.newBuilder()
                .setChannel(channel.toProto())
                .setSender(sender.toProto())
                .setText(text)
                .build();
    }
}
