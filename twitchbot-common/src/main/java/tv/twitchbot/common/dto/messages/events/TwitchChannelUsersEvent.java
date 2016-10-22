package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.RplNameReplyCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Fired each time we receive part of a /NAMES response
 * @author Cobi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#names">Twitch-API Documentation</a>
 */
public class TwitchChannelUsersEvent extends Event {
    @SuppressWarnings("unchecked")
    public static TwitchChannelUsersEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.TwitchChannelUsersEvent twitchChannelUsersEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchChannelUsersEvent.getChannel());
        final List<TwitchUser> users = (List<TwitchUser>) (List) twitchChannelUsersEvent.getUsersList().stream().map(User::fromProto).collect(Collectors.toList());
        final RplNameReplyCommand rplNameReplyCommand = (RplNameReplyCommand) IrcStanza.fromProto(twitchChannelUsersEvent.getRplNameReplyCommand());
        return new TwitchChannelUsersEvent(module, uuid, timestamp, channel, users, rplNameReplyCommand);
    }

    private final TwitchChannel channel;
    private final List<TwitchUser> users;

    private final RplNameReplyCommand rplNameReplyCommand;

    public TwitchChannelUsersEvent(final Module from, final TwitchChannel channel, final List<TwitchUser> users, final RplNameReplyCommand rplNameReplyCommand) {
        super(from);
        this.channel = channel;
        this.users = users;
        this.rplNameReplyCommand = rplNameReplyCommand;
    }

    public TwitchChannelUsersEvent(final Module from, final UUID messageId, final long timestamp, final TwitchChannel channel, final List<TwitchUser> users, final RplNameReplyCommand rplNameReplyCommand) {
        super(from, messageId, timestamp);
        this.channel = channel;
        this.users = users;
        this.rplNameReplyCommand = rplNameReplyCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public List<TwitchUser> getUsers() {
        return users;
    }

    public RplNameReplyCommand getRplNameReplyCommand() {
        return rplNameReplyCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_CHANNEL_USERS)
                .setExtension(EventOuterClass.TwitchChannelUsersEvent.data, EventOuterClass.TwitchChannelUsersEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .addAllUsers(users.stream().map(User::toProto).collect(Collectors.toList()))
                        .setRplNameReplyCommand(rplNameReplyCommand.toProto())
                        .build()
                );
    }
}
