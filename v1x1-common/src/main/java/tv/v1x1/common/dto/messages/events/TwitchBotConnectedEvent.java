package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.RplEndOfMotdCommand;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when a {@link TwitchBot} connecting to TMI is ready to send commands
 * @author Cobi
 */
public class TwitchBotConnectedEvent extends Event {
    public static TwitchBotConnectedEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.TwitchBotConnectedEvent twitchBotConnectedEvent) {
        final TwitchBot bot = (TwitchBot) Bot.fromProto(twitchBotConnectedEvent.getBot());
        final RplEndOfMotdCommand rplEndOfMotdCommand = (RplEndOfMotdCommand) IrcStanza.fromProto(twitchBotConnectedEvent.getRplEndOfMotdCommand());
        return new TwitchBotConnectedEvent(module, uuid, timestamp, bot, rplEndOfMotdCommand);
    }

    private final TwitchBot bot;

    private final RplEndOfMotdCommand rplEndOfMotdCommand;

    public TwitchBotConnectedEvent(final Module from, final TwitchBot bot, final RplEndOfMotdCommand rplEndOfMotdCommand) {
        super(from);
        this.bot = bot;
        this.rplEndOfMotdCommand = rplEndOfMotdCommand;
    }

    public TwitchBotConnectedEvent(final Module from, final UUID messageId, final long timestamp, final TwitchBot bot, final RplEndOfMotdCommand rplEndOfMotdCommand) {
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
