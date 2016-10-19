package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.BotOuterClass;
import tv.twitchbot.common.dto.proto.core.PlatformOuterClass;

/**
 * Represents a {@link Bot} on the Twitch platform
 * @author Cobi
 */
public class TwitchBot extends Bot {
    public static TwitchBot fromProto(String name, BotOuterClass.TwitchBot twitchBot) {
        return new TwitchBot(name);
    }

    public TwitchBot(String name) {
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
