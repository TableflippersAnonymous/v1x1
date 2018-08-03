package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.NoticeCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a notice from TMI regarding a {@link TwitchChannel} is received
 * @author Naomi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#notice">Twitch-API Documentation</a>
 */
public class TwitchChannelEvent extends Event {
    public static TwitchChannelEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchChannelEvent twitchChannelEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchChannelEvent.getChannel());
        final String message = twitchChannelEvent.getMessage();
        final NoticeCommand noticeCommand = (NoticeCommand) IrcStanza.fromProto(twitchChannelEvent.getNoticeCommand());
        return new TwitchChannelEvent(module, uuid, timestamp, context, channel, message, noticeCommand);
    }

    private final TwitchChannel channel;
    private final String message;

    private final NoticeCommand noticeCommand;

    public TwitchChannelEvent(final Module from, final TwitchChannel channel, final String message, final NoticeCommand noticeCommand) {
        super(from);
        this.channel = channel;
        this.message = message;
        this.noticeCommand = noticeCommand;
    }

    public TwitchChannelEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchChannel channel, final String message, final NoticeCommand noticeCommand) {
        super(from, messageId, timestamp, context);
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
