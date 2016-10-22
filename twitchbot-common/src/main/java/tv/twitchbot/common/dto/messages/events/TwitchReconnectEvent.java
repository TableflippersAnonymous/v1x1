package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Bot;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.TwitchBot;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.ReconnectCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when we are requested to reconnect to TMI
 * @author Naomi
 */
public class TwitchReconnectEvent extends Event {
    public static TwitchReconnectEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.TwitchReconnectEvent twitchReconnectEvent) {
        final TwitchBot bot = (TwitchBot) Bot.fromProto(twitchReconnectEvent.getBot());
        final ReconnectCommand reconnectCommand = (ReconnectCommand) IrcStanza.fromProto(twitchReconnectEvent.getReconnectCommand());
        return new TwitchReconnectEvent(module, uuid, timestamp, bot, reconnectCommand);
    }

    private final TwitchBot bot;

    private final ReconnectCommand reconnectCommand;

    public TwitchReconnectEvent(final Module from, final TwitchBot bot, final ReconnectCommand reconnectCommand) {
        super(from);
        this.bot = bot;
        this.reconnectCommand = reconnectCommand;
    }

    public TwitchReconnectEvent(final Module from, final UUID messageId, final long timestamp, final TwitchBot bot, final ReconnectCommand reconnectCommand) {
        super(from, messageId, timestamp);
        this.bot = bot;
        this.reconnectCommand = reconnectCommand;
    }

    public TwitchBot getBot() {
        return bot;
    }

    public ReconnectCommand getReconnectCommand() {
        return reconnectCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_RECONNECT)
                .setExtension(EventOuterClass.TwitchReconnectEvent.data, EventOuterClass.TwitchReconnectEvent.newBuilder()
                        .setBot(bot.toProto())
                        .setReconnectCommand(reconnectCommand.toProto())
                        .build()
                );
    }
}
