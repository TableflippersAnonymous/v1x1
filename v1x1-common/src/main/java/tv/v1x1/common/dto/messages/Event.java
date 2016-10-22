package tv.v1x1.common.dto.messages;

import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.messages.events.*;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;
import tv.v1x1.common.dto.proto.messages.MessageOuterClass;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Event extends Message {
    public static Event fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.Event event) {
        switch(event.getType()) {
            case CHAT_JOIN: return ChatJoinEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.ChatJoinEvent.data));
            case CHAT_MESSAGE: return ChatMessageEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.ChatMessageEvent.data));
            case CHAT_PART: return ChatPartEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.ChatPartEvent.data));
            case TWITCH_BOT_CHANNEL_STATE: return TwitchBotChannelStateEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchBotChannelStateEvent.data));
            case TWITCH_BOT_CONNECTED: return TwitchBotConnectedEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchBotConnectedEvent.data));
            case TWITCH_BOT_GLOBAL_STATE: return TwitchBotGlobalStateEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchBotGlobalStateEvent.data));
            case TWITCH_CHANNEL_EVENT: return TwitchChannelEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchChannelEvent.data));
            case TWITCH_CHANNEL_USERS: return TwitchChannelUsersEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchChannelUsersEvent.data));
            case TWITCH_HOST: return TwitchHostEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchHostEvent.data));
            case TWITCH_PING: return TwitchPingEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchPingEvent.data));
            case TWITCH_RAW_MESSAGE: return TwitchRawMessageEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchRawMessageEvent.data));
            case TWITCH_RECONNECT: return TwitchReconnectEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchReconnectEvent.data));
            case TWITCH_ROOM_STATE: return TwitchRoomStateEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchRoomStateEvent.data));
            case TWITCH_TIMEOUT: return TwitchTimeoutEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchTimeoutEvent.data));
            case TWITCH_USER: return TwitchUserEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchUserEvent.data));
            case TWITCH_USER_MOD_CHANGE: return TwitchUserModChangeEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.TwitchUserModChangeEvent.data));
            case SCHEDULER_NOTIFY: return SchedulerNotifyEvent.fromProto(module, uuid, timestamp, event.getExtension(EventOuterClass.SchedulerNotifyEvent.data));
            default: throw new IllegalStateException("Unknown event type " + event.getType().name());
        }
    }

    public Event(final Module from) {
        super(from);
    }

    public Event(final Module from, final UUID messageId, final long timestamp) {
        super(from, messageId, timestamp);
    }

    protected EventOuterClass.Event.Builder toProtoEvent() {
        return EventOuterClass.Event.newBuilder();
    }

    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return super.toProtoMessage()
                .setType(MessageOuterClass.Message.MessageType.EVENT)
                .setExtension(EventOuterClass.Event.data, toProtoEvent().build());
    }
}
