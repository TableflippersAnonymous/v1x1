package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link User} leaves a {@link Channel}
 * @author Cobi
 */
public abstract class ChatPartEvent extends Event {
    public static ChatPartEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.ChatPartEvent chatPartEvent) {
        final User user = User.fromProto(chatPartEvent.getUser());
        final Channel channel = Channel.fromProto(chatPartEvent.getChannel());
        switch(chatPartEvent.getType()) {
            case TWITCH: return TwitchChatPartEvent.fromProto(module, uuid, timestamp, context, user, channel, chatPartEvent.getExtension(EventOuterClass.TwitchChatPartEvent.data));
            default: throw new IllegalStateException("Unknown ChatPartEvent type: " + chatPartEvent.getType());
        }
    }

    private final User user;
    private final Channel channel;

    public ChatPartEvent(final Module from, final User user, final Channel channel) {
        super(from);
        this.user = user;
        this.channel = channel;
    }

    public ChatPartEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final User user, final Channel channel) {
        super(from, messageId, timestamp, context);
        this.user = user;
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.CHAT_PART)
                .setExtension(EventOuterClass.ChatPartEvent.data, toProtoChatPart().build());
    }

    protected EventOuterClass.ChatPartEvent.Builder toProtoChatPart() {
        return EventOuterClass.ChatPartEvent.newBuilder()
                .setUser(user.toProto())
                .setChannel(channel.toProto());
    }
}
