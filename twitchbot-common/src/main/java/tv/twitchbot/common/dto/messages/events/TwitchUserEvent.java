package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.UserNoticeCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when we receive a re-subscription notice
 * @author Cobi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#usernotice-1">Twitch-API Documentation</a>
 */
public class TwitchUserEvent extends Event {
    public static TwitchUserEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.TwitchUserEventOrBuilder twitchUserEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchUserEvent.getChannel());
        final TwitchUser user = (TwitchUser) User.fromProto(twitchUserEvent.getUser());
        final String message = twitchUserEvent.getMessage();
        final UserNoticeCommand userNoticeCommand = (UserNoticeCommand) IrcStanza.fromProto(twitchUserEvent.getUserNoticeCommand());
        return new TwitchUserEvent(module, uuid, timestamp, channel, user, message, userNoticeCommand);
    }

    private final TwitchChannel channel;
    private final TwitchUser user;
    private final String message;

    private final UserNoticeCommand userNoticeCommand;

    public TwitchUserEvent(final Module from, final TwitchChannel channel, final TwitchUser user, final String message, final UserNoticeCommand userNoticeCommand) {
        super(from);
        this.channel = channel;
        this.user = user;
        this.message = message;
        this.userNoticeCommand = userNoticeCommand;
    }

    public TwitchUserEvent(final Module from, final UUID messageId, final long timestamp, final TwitchChannel channel, final TwitchUser user, final String message, final UserNoticeCommand userNoticeCommand) {
        super(from, messageId, timestamp);
        this.channel = channel;
        this.user = user;
        this.message = message;
        this.userNoticeCommand = userNoticeCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchUser getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public UserNoticeCommand getUserNoticeCommand() {
        return userNoticeCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_USER)
                .setExtension(EventOuterClass.TwitchUserEvent.data, EventOuterClass.TwitchUserEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setUser(user.toProto())
                        .setMessage(message)
                        .setUserNoticeCommand(userNoticeCommand.toProto())
                        .build()
                );
    }
}
