package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.proto.core.ChatMessageOuterClass;

/**
 * Created by cobi on 11/4/2016.
 */
public class PrivateMessage {
    public static PrivateMessage fromProto(final ChatMessageOuterClass.PrivateMessage message) {
        final Bot target = Bot.fromProto(message.getTarget());
        final User sender = User.fromProto(message.getSender());
        final String text = message.getText();
        return new PrivateMessage(target, sender, text);
    }

    private final Bot target;
    private final User sender;
    private final String text;

    public PrivateMessage(final Bot target, final User sender, final String text) {
        this.target = target;
        this.sender = sender;
        this.text = text;
    }

    public Bot getTarget() {
        return target;
    }

    public User getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public ChatMessageOuterClass.PrivateMessage toProto() {
        return ChatMessageOuterClass.PrivateMessage.newBuilder()
                .setTarget(target.toProto())
                .setSender(sender.toProto())
                .setText(text)
                .build();
    }
}
