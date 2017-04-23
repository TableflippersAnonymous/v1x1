package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Bot;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchBot;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.ReconnectCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when we are requested to reconnect to TMI
 * @author Cobi
 */
public class TwitchReconnectEvent extends Event {
    public static TwitchReconnectEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchReconnectEvent twitchReconnectEvent) {
        final TwitchBot bot = (TwitchBot) Bot.fromProto(twitchReconnectEvent.getBot());
        final ReconnectCommand reconnectCommand = (ReconnectCommand) IrcStanza.fromProto(twitchReconnectEvent.getReconnectCommand());
        return new TwitchReconnectEvent(module, uuid, timestamp, context, bot, reconnectCommand);
    }

    private final TwitchBot bot;

    private final ReconnectCommand reconnectCommand;

    public TwitchReconnectEvent(final Module from, final TwitchBot bot, final ReconnectCommand reconnectCommand) {
        super(from);
        this.bot = bot;
        this.reconnectCommand = reconnectCommand;
    }

    public TwitchReconnectEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchBot bot, final ReconnectCommand reconnectCommand) {
        super(from, messageId, timestamp, context);
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
