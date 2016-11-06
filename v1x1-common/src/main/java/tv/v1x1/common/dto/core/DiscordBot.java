package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.BotOuterClass;
import tv.v1x1.common.dto.proto.core.PlatformOuterClass;

/**
 * Represents a {@link Bot} on the Discord platform
 * @author Cobi
 */
public class DiscordBot extends Bot {
    public static DiscordBot fromProto(final String name, final BotOuterClass.DiscordBot discordBot) {
        return new DiscordBot(name);
    }

    public DiscordBot(final String name) {
        super(name);
    }

    @Override
    public Platform getPlatform() {
        return Platform.DISCORD;
    }

    @Override
    protected BotOuterClass.Bot.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setType(PlatformOuterClass.Platform.DISCORD)
                .setExtension(BotOuterClass.DiscordBot.data, toProtoDiscordBot());
    }

    private BotOuterClass.DiscordBot toProtoDiscordBot() {
        return BotOuterClass.DiscordBot.newBuilder()
                .build();
    }
}
