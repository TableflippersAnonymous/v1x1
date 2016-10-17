package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.NoticeCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a notice from TMI regarding a {@link TwitchChannel} is received
 * @author Cobi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#notice">Twitch-API Documentation</a>
 */
public class TwitchChannelEvent extends Event {
    public static TwitchChannelEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchChannelEvent twitchChannelEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchChannelEvent.getChannel());
        String message = twitchChannelEvent.getMessage();
        NoticeCommand noticeCommand = (NoticeCommand) IrcStanza.fromProto(twitchChannelEvent.getNoticeCommand());
        return new TwitchChannelEvent(module, uuid, timestamp, channel, message, noticeCommand);
    }

    private TwitchChannel channel;
    private String message;

    private NoticeCommand noticeCommand;

    public TwitchChannelEvent(Module from, TwitchChannel channel, String message, NoticeCommand noticeCommand) {
        super(from);
        this.channel = channel;
        this.message = message;
        this.noticeCommand = noticeCommand;
    }

    public TwitchChannelEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, String message, NoticeCommand noticeCommand) {
        super(from, messageId, timestamp);
        this.channel = channel;
        this.message = message;
        this.noticeCommand = noticeCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public NoticeCommand getNoticeCommand() {
        return noticeCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_CHANNEL_EVENT)
                .setExtension(EventOuterClass.TwitchChannelEvent.data, EventOuterClass.TwitchChannelEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setMessage(message)
                        .setNoticeCommand(noticeCommand.toProto())
                        .build()
                );
    }
}
