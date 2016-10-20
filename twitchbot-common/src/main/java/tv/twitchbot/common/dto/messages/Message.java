package tv.twitchbot.common.dto.messages;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.proto.core.BotOuterClass;
import tv.twitchbot.common.dto.proto.core.IRC;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;
import tv.twitchbot.common.dto.proto.messages.MessageOuterClass;
import tv.twitchbot.common.dto.proto.messages.RequestOuterClass;

import java.lang.invoke.MethodHandles;
import java.util.Date;

/**
 * Created by cobi on 10/4/16.
 */
public abstract class Message {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();

    static {
        /* On Message */
        Message.register(EventOuterClass.Event.data);
        Message.register(RequestOuterClass.Request.data);
        Message.register(RequestOuterClass.Response.data);

        /* On Event */
        Message.register(EventOuterClass.ChatJoinEvent.data);
        Message.register(EventOuterClass.ChatMessageEvent.data);
        Message.register(EventOuterClass.ChatPartEvent.data);
        Message.register(EventOuterClass.TwitchBotChannelStateEvent.data);
        Message.register(EventOuterClass.TwitchBotConnectedEvent.data);
        Message.register(EventOuterClass.TwitchBotGlobalStateEvent.data);
        Message.register(EventOuterClass.TwitchChannelEvent.data);
        Message.register(EventOuterClass.TwitchChannelUsersEvent.data);
        Message.register(EventOuterClass.TwitchHostEvent.data);
        Message.register(EventOuterClass.TwitchPingEvent.data);
        Message.register(EventOuterClass.TwitchRawMessageEvent.data);
        Message.register(EventOuterClass.TwitchReconnectEvent.data);
        Message.register(EventOuterClass.TwitchRoomStateEvent.data);
        Message.register(EventOuterClass.TwitchTimeoutEvent.data);
        Message.register(EventOuterClass.TwitchUserEvent.data);
        Message.register(EventOuterClass.TwitchUserModChangeEvent.data);

        /* On independent events */
        Message.register(EventOuterClass.TwitchChatJoinEvent.data);
        Message.register(EventOuterClass.TwitchChatMessageEvent.data);
        Message.register(EventOuterClass.TwitchChatPartEvent.data);

        /* On Bot */
        Message.register(BotOuterClass.TwitchBot.data);
        Message.register(BotOuterClass.DiscordBot.data);

        /* On IRC */
        Message.register(IRC.IrcServer.data);
        Message.register(IRC.IrcUser.data);
        Message.register(IRC.ClearChatCommand.data);
        Message.register(IRC.GlobalUserStateCommand.data);
        Message.register(IRC.HostTargetCommand.data);
        Message.register(IRC.JoinCommand.data);
        Message.register(IRC.ModeCommand.data);
        Message.register(IRC.NoticeCommand.data);
        Message.register(IRC.PartCommand.data);
        Message.register(IRC.PingCommand.data);
        Message.register(IRC.PrivmsgCommand.data);
        Message.register(IRC.ReconnectCommand.data);
        Message.register(IRC.RoomStateCommand.data);
        Message.register(IRC.RplNameReplyCommand.data);
        Message.register(IRC.RplEndOfMotdCommand.data);
        Message.register(IRC.UserNoticeCommand.data);
        Message.register(IRC.UserStateCommand.data);

        /* On Request & Response */
        Message.register(RequestOuterClass.ModuleShutdownRequest.data);
        Message.register(RequestOuterClass.ModuleShutdownResponse.data);
        Message.register(RequestOuterClass.SendMessageRequest.data);
        Message.register(RequestOuterClass.SendMessageResponse.data);
    }

    public static void register(GeneratedMessage.GeneratedExtension<?, ?> field) {
        extensionRegistry.add(field);
    }

    public static Message fromBytes(byte[] bytes) throws InvalidProtocolBufferException {
        MessageOuterClass.Message message = MessageOuterClass.Message.parseFrom(bytes, extensionRegistry);
        try {
            return fromProto(message);
        } catch(Exception e) {
            LOG.error("Exception converting: {}", message, e);
            throw e;
        }
    }

    public static Message fromProto(MessageOuterClass.Message message) {
        Module module = Module.fromProto(message.getFrom());
        UUID uuid = UUID.fromProto(message.getMessageId());
        long timestamp = message.getTimestamp();
        switch(message.getType()) {
            case EVENT: return Event.fromProto(module, uuid, timestamp, message.getExtension(EventOuterClass.Event.data));
            case REQUEST: return Request.fromProto(module, uuid, timestamp, message.getExtension(RequestOuterClass.Request.data));
            case RESPONSE: return Response.fromProto(module, uuid, timestamp, message.getExtension(RequestOuterClass.Response.data));
            default: throw new IllegalStateException("Unknown message type " + message.getType().name());
        }
    }

    private Module from;
    private UUID messageId;
    private long timestamp;

    public Message(Module from) {
        this(from, new UUID(java.util.UUID.randomUUID()), new Date().getTime());
    }

    public Message(Module from, UUID messageId, long timestamp) {
        this.from = from;
        this.messageId = messageId;
        this.timestamp = timestamp;
    }

    public Module getFrom() {
        return from;
    }

    /**
     * Represents the (queue) message ID dropped onto the message queue
     * @return UUID the message ID
     */
    public UUID getMessageId() {
        return messageId;
    }

    /**
     * Represents the timestamp the (queue) message was created locally
     * @return long the timestamp we put it on
     */
    public long getTimestamp() {
        return timestamp;
    }

    public MessageOuterClass.Message toProto() {
        return toProtoMessage().build();
    }

    protected MessageOuterClass.Message.Builder toProtoMessage() {
        return MessageOuterClass.Message.newBuilder()
                .setFrom(from.toProto())
                .setMessageId(messageId.toProto())
                .setTimestamp(timestamp);
    }

    public byte[] toBytes() {
        return toProto().toByteArray();
    }
}
