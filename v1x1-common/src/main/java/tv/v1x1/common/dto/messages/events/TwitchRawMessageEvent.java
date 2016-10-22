package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Bot;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchBot;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired for each line ingested from the socket connecting to TMI
 * @author Cobi
 */
public class TwitchRawMessageEvent extends Event {
    public static TwitchRawMessageEvent fromProto(final Module module, final UUID uuid, final long timestamp, final EventOuterClass.TwitchRawMessageEvent twitchRawMessageEvent) {
        final TwitchBot bot = (TwitchBot) Bot.fromProto(twitchRawMessageEvent.getBot());
        final IrcStanza ircStanza = IrcStanza.fromProto(twitchRawMessageEvent.getIrcStanza());
        return new TwitchRawMessageEvent(module, uuid, timestamp, bot, ircStanza);
    }

    private final TwitchBot bot;
    private final IrcStanza ircStanza;

    public TwitchRawMessageEvent(final Module from, final TwitchBot bot, final IrcStanza ircStanza) {
        super(from);
        this.bot = bot;
        this.ircStanza = ircStanza;
    }

    public TwitchRawMessageEvent(final Module from, final UUID messageId, final long timestamp, final TwitchBot bot, final IrcStanza ircStanza) {
        super(from, messageId, timestamp);
        this.bot = bot;
        this.ircStanza = ircStanza;
    }

    public TwitchBot getBot() {
        return bot;
    }

    public IrcStanza getIrcStanza() {
        return ircStanza;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_RAW_MESSAGE)
                .setExtension(EventOuterClass.TwitchRawMessageEvent.data, EventOuterClass.TwitchRawMessageEvent.newBuilder()
                        .setBot(bot.toProto())
                        .setIrcStanza(ircStanza.toProto())
                        .build()
                );
    }
}
