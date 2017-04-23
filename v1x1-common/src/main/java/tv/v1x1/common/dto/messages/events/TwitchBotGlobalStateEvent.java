package tv.v1x1.common.dto.messages.events;

import tv.v1x1.common.dto.core.Bot;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.TwitchBot;
import tv.v1x1.common.dto.core.UUID;
import tv.v1x1.common.dto.irc.IrcStanza;
import tv.v1x1.common.dto.irc.commands.GlobalUserStateCommand;
import tv.v1x1.common.dto.messages.Context;
import tv.v1x1.common.dto.messages.Event;
import tv.v1x1.common.dto.proto.messages.EventOuterClass;

/**
 * Fired when the global state of a {@link TwitchBot} is reported
 * @author Cobi
 * @see <a href="https://github.com/justintv/Twitch-API/blob/master/IRC.md#globaluserstate">Twitch-API documentation</a>
 */
public class TwitchBotGlobalStateEvent extends Event {
    public static TwitchBotGlobalStateEvent fromProto(final Module module, final UUID uuid, final long timestamp, final Context context, final EventOuterClass.TwitchBotGlobalStateEvent twitchBotGlobalStateEvent) {
        final TwitchBot bot = (TwitchBot) Bot.fromProto(twitchBotGlobalStateEvent.getBot());
        final GlobalUserStateCommand globalUserStateCommand = (GlobalUserStateCommand) IrcStanza.fromProto(twitchBotGlobalStateEvent.getGlobalUserStateCommand());
        return new TwitchBotGlobalStateEvent(module, uuid, timestamp, context, bot, globalUserStateCommand);
    }

    private final TwitchBot bot;

    private final GlobalUserStateCommand globalUserStateCommand;

    public TwitchBotGlobalStateEvent(final Module from, final TwitchBot bot, final GlobalUserStateCommand globalUserStateCommand) {
        super(from);
        this.bot = bot;
        this.globalUserStateCommand = globalUserStateCommand;
    }

    public TwitchBotGlobalStateEvent(final Module from, final UUID messageId, final long timestamp, final Context context, final TwitchBot bot, final GlobalUserStateCommand globalUserStateCommand) {
        super(from, messageId, timestamp, context);
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
