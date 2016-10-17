package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.*;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.RplEndOfMotdCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link TwitchBot} connecting to TMI is ready to send commands
 * @author Cobi
 */
public class TwitchBotConnectedEvent extends Event {
    public static TwitchBotConnectedEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchBotConnectedEvent twitchBotConnectedEvent) {
        TwitchBot bot = (TwitchBot) Bot.fromProto(twitchBotConnectedEvent.getBot());
        RplEndOfMotdCommand rplEndOfMotdCommand = (RplEndOfMotdCommand) IrcStanza.fromProto(twitchBotConnectedEvent.getRplEndOfMotdCommand());
        return new TwitchBotConnectedEvent(module, uuid, timestamp, bot, rplEndOfMotdCommand);
    }

    private TwitchBot bot;

    private RplEndOfMotdCommand rplEndOfMotdCommand;

    public TwitchBotConnectedEvent(Module from, TwitchBot bot, RplEndOfMotdCommand rplEndOfMotdCommand) {
        super(from);
        this.bot = bot;
        this.rplEndOfMotdCommand = rplEndOfMotdCommand;
    }

    public TwitchBotConnectedEvent(Module from, UUID messageId, long timestamp, TwitchBot bot, RplEndOfMotdCommand rplEndOfMotdCommand) {
        super(from, messageId, timestamp);
        this.bot = bot;
        this.rplEndOfMotdCommand = rplEndOfMotdCommand;
    }

    public TwitchBot getBot() {
        return bot;
    }

    public RplEndOfMotdCommand getRplEndOfMotdCommand() {
        return rplEndOfMotdCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_BOT_CONNECTED)
                .setExtension(EventOuterClass.TwitchBotConnectedEvent.data, EventOuterClass.TwitchBotConnectedEvent.newBuilder()
                        .setBot(bot.toProto())
                        .setRplEndOfMotdCommand(rplEndOfMotdCommand.toProto())
                        .build()
                );
    }
}
