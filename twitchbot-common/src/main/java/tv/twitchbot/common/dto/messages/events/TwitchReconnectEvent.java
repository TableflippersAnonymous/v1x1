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
 * Created by naomi on 10/9/2016.
 */
public class TwitchReconnectEvent extends Event {
    public static TwitchReconnectEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchReconnectEvent twitchReconnectEvent) {
        TwitchBot bot = (TwitchBot) Bot.fromProto(twitchReconnectEvent.getBot());
        ReconnectCommand reconnectCommand = (ReconnectCommand) IrcStanza.fromProto(twitchReconnectEvent.getReconnectCommand());
        return new TwitchReconnectEvent(module, uuid, timestamp, bot, reconnectCommand);
    }

    private TwitchBot bot;

    private ReconnectCommand reconnectCommand;

    public TwitchReconnectEvent(Module from, TwitchBot bot, ReconnectCommand reconnectCommand) {
        super(from);
        this.bot = bot;
        this.reconnectCommand = reconnectCommand;
    }

    public TwitchReconnectEvent(Module from, UUID messageId, long timestamp, TwitchBot bot, ReconnectCommand reconnectCommand) {
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
