package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.BotOuterClass;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;

/**
 * Represents a {@link Bot} on the Twitch platform
 * @author Cobi
 */
public class TwitchBot extends Bot {
    public static TwitchBot fromProto(final String name, final BotOuterClass.TwitchBot twitchBot) {
        return new TwitchBot(name);
    }

    public TwitchBot(final String name) {
        super(name);
    }

    @Override
    public Platform getPlatform() {
        return Platform.TWITCH;
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
