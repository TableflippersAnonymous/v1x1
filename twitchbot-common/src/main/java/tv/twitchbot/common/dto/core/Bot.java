package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.BotOuterClass;

/**
 * Represents a single username on a single platform on which we run
 * @author Cobi
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

    public BotOuterClass.Bot toProto() {
        return toProtoBuilder().build();
    }

    protected BotOuterClass.Bot.Builder toProtoBuilder() {
        return BotOuterClass.Bot.newBuilder()
                .setName(name);
    }
}
