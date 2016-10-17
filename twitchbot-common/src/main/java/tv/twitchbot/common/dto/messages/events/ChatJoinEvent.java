package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Channel;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.core.User;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link User} joining a {@link Channel}
 * @author Naomi
 */
public abstract class ChatJoinEvent extends Event {
    public static ChatJoinEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.ChatJoinEvent chatJoinEvent) {
        User user = User.fromProto(chatJoinEvent.getUser());
        Channel channel = Channel.fromProto(chatJoinEvent.getChannel());
        switch(chatJoinEvent.getType()) {
            case TWITCH: return TwitchChatJoinEvent.fromProto(module, uuid, timestamp, user, channel, chatJoinEvent.getExtension(EventOuterClass.TwitchChatJoinEvent.data));
            default: throw new IllegalStateException("Unknown ChatJoinEvent type: " + chatJoinEvent.getType());
        }
    }

    private User user;
    private Channel channel;

    /**
     * Construct a new event using the current time and a random UUID
     */
    public ChatJoinEvent(Module from, User user, Channel channel) {
        super(from);
        this.user = user;
        this.channel = channel;
    }

    /**
     * Construct a new event using your own UUID and time
     * For example, for deserialization of a saved event
     */
    public ChatJoinEvent(Module from, UUID messageId, long timestamp, User user, Channel channel) {
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
                .setExtension(EventOuterClass.ChatJoinEvent.data, toProtoChatJoin().build());
    }

    protected EventOuterClass.ChatJoinEvent.Builder toProtoChatJoin() {
        return EventOuterClass.ChatJoinEvent.newBuilder()
                .setUser(user.toProto())
                .setChannel(channel.toProto());
    }
}
