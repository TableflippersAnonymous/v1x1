package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.UserNoticeCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/9/2016.
 */
public class TwitchUserEvent extends Event {
    public static TwitchUserEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchUserEvent twitchUserEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchUserEvent.getChannel());
        TwitchUser user = (TwitchUser) User.fromProto(twitchUserEvent.getUser());
        String message = twitchUserEvent.getMessage();
        UserNoticeCommand userNoticeCommand = (UserNoticeCommand) IrcStanza.fromProto(twitchUserEvent.getUserNoticeCommand());
        return new TwitchUserEvent(module, uuid, timestamp, channel, user, message, userNoticeCommand);
    }

    private TwitchChannel channel;
    private TwitchUser user;
    private String message;

    private UserNoticeCommand userNoticeCommand;

    public TwitchUserEvent(Module from, TwitchChannel channel, TwitchUser user, String message, UserNoticeCommand userNoticeCommand) {
        super(from);
        this.channel = channel;
        this.user = user;
        this.message = message;
        this.userNoticeCommand = userNoticeCommand;
    }

    public TwitchUserEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, TwitchUser user, String message, UserNoticeCommand userNoticeCommand) {
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
