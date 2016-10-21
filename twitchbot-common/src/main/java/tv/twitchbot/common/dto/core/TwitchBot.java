package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.BotOuterClass;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;

/**
 * Represents a {@link Bot} on the Twitch platform
 * @author Naomi
 */
public class TwitchBot extends Bot {
    public static TwitchBot fromProto(final String name, final BotOuterClass.TwitchBot twitchBot) {
        return new TwitchBot(name);
    }

    public TwitchBot(final String name) {
        super(name);
    }

    @Override
    protected BotOuterClass.Bot.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setType(PlatformOuterClass.Platform.TWITCH)
                .setExtension(BotOuterClass.TwitchBot.data, toProtoTwitchBot());
    }

    private BotOuterClass.TwitchBot toProtoTwitchBot() {
        return BotOuterClass.TwitchBot.newBuilder()
                .build();
    }
}
