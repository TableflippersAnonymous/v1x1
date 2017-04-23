package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.UserStateCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when the state of a {@link TwitchBot} in a {@link TwitchChannel} is sent
 * @author Naomi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#userstate-1">Twitch-API documentation</a>
 */
public class TwitchBotChannelStateEvent extends Event {
    public static TwitchBotChannelStateEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchBotChannelStateEvent twitchBotChannelStateEvent) {
        final TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchBotChannelStateEvent.getChannel());
        final TwitchBot bot = (TwitchBot) Bot.fromProto(twitchBotChannelStateEvent.getBot());
        final UserStateCommand userStateCommand = (UserStateCommand) IrcStanza.fromProto(twitchBotChannelStateEvent.getUserStateCommand());
        return new TwitchBotChannelStateEvent(module, uuid, timestamp, context, channel, bot, userStateCommand);
    }

    private final TwitchChannel channel;
    private final TwitchBot bot;

    private final UserStateCommand userStateCommand;

    public TwitchBotChannelStateEvent(final Module from, final TwitchChannel channel, final TwitchBot bot, final UserStateCommand userStateCommand) {
        super(from);
        this.channel = channel;
        this.bot = bot;
        this.userStateCommand = userStateCommand;
    }

    public TwitchBotChannelStateEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchChannel channel, final TwitchBot bot, final UserStateCommand userStateCommand) {
        super(from, messageId, timestamp, context);
        this.channel = channel;
        this.bot = bot;
        this.userStateCommand = userStateCommand;
    }

    public TwitchChannel getChannel() {
        return channel;
    }

    public TwitchBot getBot() {
        return bot;
    }

    public UserStateCommand getUserStateCommand() {
        return userStateCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_BOT_CHANNEL_STATE)
                .setExtension(EventOuterClass.TwitchBotChannelStateEvent.data, EventOuterClass.TwitchBotChannelStateEvent.newBuilder()
                        .setChannel(channel.toProto())
                        .setBot(bot.toProto())
                        .setUserStateCommand(userStateCommand.toProto())
                        .build()
                );
    }
}
