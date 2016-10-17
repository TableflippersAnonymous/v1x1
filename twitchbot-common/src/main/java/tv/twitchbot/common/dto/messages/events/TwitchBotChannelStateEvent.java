package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.UserStateCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when the state of a {@link TwitchBot} in a {@link TwitchChannel} is sent
 * @author Naomi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#userstate-1">Twitch-API documentation</a>
 */
public class TwitchBotChannelStateEvent extends Event {
    public static TwitchBotChannelStateEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchBotChannelStateEvent twitchBotChannelStateEvent) {
        TwitchChannel channel = (TwitchChannel) Channel.fromProto(twitchBotChannelStateEvent.getChannel());
        TwitchBot bot = (TwitchBot) Bot.fromProto(twitchBotChannelStateEvent.getBot());
        UserStateCommand userStateCommand = (UserStateCommand) IrcStanza.fromProto(twitchBotChannelStateEvent.getUserStateCommand());
        return new TwitchBotChannelStateEvent(module, uuid, timestamp, channel, bot, userStateCommand);
    }

    private TwitchChannel channel;
    private TwitchBot bot;

    private UserStateCommand userStateCommand;

    public TwitchBotChannelStateEvent(Module from, TwitchChannel channel, TwitchBot bot, UserStateCommand userStateCommand) {
        super(from);
        this.channel = channel;
        this.bot = bot;
        this.userStateCommand = userStateCommand;
    }

    public TwitchBotChannelStateEvent(Module from, UUID messageId, long timestamp, TwitchChannel channel, TwitchBot bot, UserStateCommand userStateCommand) {
        super(from, messageId, timestamp);
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
