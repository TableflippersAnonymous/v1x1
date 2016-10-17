package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.core.User;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link User} leaves a {@link Channel}
 * @author Naomi
 */
public abstract class ChatPartEvent extends Event {
    public static ChatPartEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.ChatPartEvent chatPartEvent) {
        User user = User.fromProto(chatPartEvent.getUser());
        Channel channel = Channel.fromProto(chatPartEvent.getChannel());
        switch(chatPartEvent.getType()) {
            case TWITCH: return TwitchChatPartEvent.fromProto(module, uuid, timestamp, user, channel, chatPartEvent.getExtension(EventOuterClass.TwitchChatPartEvent.data));
            default: throw new IllegalStateException("Unknown ChatPartEvent type: " + chatPartEvent.getType());
        }
    }

    private User user;
    private Channel channel;

    public ChatPartEvent(Module from, User user, Channel channel) {
        super(from);
        this.user = user;
        this.channel = channel;
    }

    public ChatPartEvent(Module from, UUID messageId, long timestamp, User user, Channel channel) {
        super(from, messageId, timestamp);
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
                .setType(EventOuterClass.Event.EventType.CHAT_JOIN)
                .setExtension(EventOuterClass.ChatPartEvent.data, toProtoChatPart().build());
    }

    protected EventOuterClass.ChatPartEvent.Builder toProtoChatPart() {
        return EventOuterClass.ChatPartEvent.newBuilder()
                .setUser(user.toProto())
                .setChannel(channel.toProto());
    }
}
