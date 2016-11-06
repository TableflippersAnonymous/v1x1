package tv.v1x1.common.dto.core;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.proto.core.BotOuterClass;

/**
 * Represents a single username on a single platform on which we run
 * @author Naomi
 */
public abstract class Bot {
    private final String name;

    public static Bot fromProto(final BotOuterClass.Bot bot) {
        final String name = bot.getName();
        switch(bot.getType()) {
            case TWITCH: return TwitchBot.fromProto(name, bot.getExtension(BotOuterClass.TwitchBot.data));
            case DISCORD: return DiscordBot.fromProto(name, bot.getExtension(BotOuterClass.DiscordBot.data));
            default: throw new IllegalStateException("Unknown Bot type: " + bot.getType());
        }
    }

    public Bot(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Platform getPlatform();

    public BotOuterClass.Bot toProto() {
        return toProtoBuilder().build();
    }

    protected BotOuterClass.Bot.Builder toProtoBuilder() {
        return BotOuterClass.Bot.newBuilder()
                .setName(name);
    }
}
