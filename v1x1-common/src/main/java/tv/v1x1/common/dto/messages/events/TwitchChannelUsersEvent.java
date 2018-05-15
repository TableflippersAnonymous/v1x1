package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.TwitchUser;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.RplNameReplyCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Fired each time we receive part of a /NAMES response
 * @author Naomi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#names">Twitch-API Documentation</a>
 */
public class TwitchChannelUsersEvent extends Event {
    private final TwitchChannel channel;
    private final List<TwitchUser> users;

    private final RplNameReplyCommand rplNameReplyCommand;

    @SuppressWarnings("unchecked")
    public static TwitchChannelUsersEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchChannelUsersEvent twitchChannelUsersEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchChannelUsersEvent.getChannel());
        final List<TwitchUser> users = (List<TwitchUser>) (List) twitchChannelUsersEvent.getUsersList().stream().map(User::fromProto).collect(Collectors.toList());
        final RplNameReplyCommand rplNameReplyCommand = (RplNameReplyCommand) IrcStanza.fromProto(twitchChannelUsersEvent.getRplNameReplyCommand());
        return new TwitchChannelUsersEvent(module, uuid, timestamp, context, channel, users, rplNameReplyCommand);
    }

    public TwitchChannelUsersEvent(final Module from, final TwitchChannel channel, final List<TwitchUser> users, final RplNameReplyCommand rplNameReplyCommand) {
        super(from);
        this.channel = channel;
        this.users = users;
        this.rplNameReplyCommand = rplNameReplyCommand;
    }

    public TwitchChannelUsersEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchChannel channel, final List<TwitchUser> users, final RplNameReplyCommand rplNameReplyCommand) {
        super(from, messageId, timestamp, context);
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
