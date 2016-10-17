package tv.twitchbot.common.dto.messages.events;

import tv.twitchbot.common.dto.core.Bot;
import tv.twitchbot.common.dto.core.Module;
import tv.twitchbot.common.dto.core.TwitchBot;
import tv.twitchbot.common.dto.core.UUID;
import tv.twitchbot.common.dto.irc.IrcStanza;
import tv.twitchbot.common.dto.irc.commands.GlobalUserStateCommand;
import tv.twitchbot.common.dto.messages.Event;
import tv.twitchbot.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when the global state of a {@link TwitchBot} is reported
 * @author Cobi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#globaluserstate">Twitch-API documentation</a>
 */
public class TwitchBotGlobalStateEvent extends Event {
    public static TwitchBotGlobalStateEvent fromProto(Module module, UUID uuid, long timestamp, EventOuterClass.TwitchBotGlobalStateEvent twitchBotGlobalStateEvent) {
        TwitchBot bot = (TwitchBot) Bot.fromProto(twitchBotGlobalStateEvent.getBot());
        GlobalUserStateCommand globalUserStateCommand = (GlobalUserStateCommand) IrcStanza.fromProto(twitchBotGlobalStateEvent.getGlobalUserStateCommand());
        return new TwitchBotGlobalStateEvent(module, uuid, timestamp, bot, globalUserStateCommand);
    }

    private TwitchBot bot;

    private GlobalUserStateCommand globalUserStateCommand;

    public TwitchBotGlobalStateEvent(Module from, TwitchBot bot, GlobalUserStateCommand globalUserStateCommand) {
        super(from);
        this.bot = bot;
        this.globalUserStateCommand = globalUserStateCommand;
    }

    public TwitchBotGlobalStateEvent(Module from, UUID messageId, long timestamp, TwitchBot bot, GlobalUserStateCommand globalUserStateCommand) {
        super(from, messageId, timestamp);
        this.bot = bot;
        this.globalUserStateCommand = globalUserStateCommand;
    }

    public TwitchBot getBot() {
        return bot;
    }

    public GlobalUserStateCommand getGlobalUserStateCommand() {
        return globalUserStateCommand;
    }

    @Override
    protected EventOuterClass.Event.Builder toProtoEvent() {
        return super.toProtoEvent()
                .setType(EventOuterClass.Event.EventType.TWITCH_BOT_GLOBAL_STATE)
                .setExtension(EventOuterClass.TwitchBotGlobalStateEvent.data, EventOuterClass.TwitchBotGlobalStateEvent.newBuilder()
                        .setBot(bot.toProto())
                        .setGlobalUserStateCommand(globalUserStateCommand.toProto())
                        .build()
                );
    }

}
