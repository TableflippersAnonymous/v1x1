package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Bot;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.TwitchBot;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Created by cobi on 10/9/2016.
 */
public class TwitchRawMessageEvent extends Event {
    public static TwitchRawMessageEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchRawMessageEvent twitchRawMessageEvent) {
        TwitchBot bot = (TwitchBot) Bot.fromProto(twitchRawMessageEvent.getBot());
        IrcStanza ircStanza = IrcStanza.fromProto(twitchRawMessageEvent.getIrcStanza());
        return new TwitchRawMessageEvent(module, uuid, timestamp, bot, ircStanza);
    }

    private TwitchBot bot;
    private IrcStanza ircStanza;

    public TwitchRawMessageEvent(Module from, TwitchBot bot, IrcStanza ircStanza) {
        super(from);
        this.bot = bot;
        this.ircStanza = ircStanza;
    }

    public TwitchRawMessageEvent(Module from, UUID messageId, long timestamp, TwitchBot bot, IrcStanza ircStanza) {
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
