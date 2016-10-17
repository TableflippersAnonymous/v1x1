package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.BotOuterClass;

/**
 * Represents a single username on a single platform on which we run
 * @author Naomi
 */
public abstract class Bot {
    public static Bot fromProto(BotOuterClass.Bot bot) {
        String name = bot.getName();
        switch(bot.getType()) {
            case TWITCH: return TwitchBot.fromProto(name, bot.getExtension(BotOuterClass.TwitchBot.data));
            case DISCORD: return DiscordBot.fromProto(name, bot.getExtension(BotOuterClass.DiscordBot.data));
            default: throw new IllegalStateException("Unknown Bot type: " + bot.getType());
        }
    }

    private String name;

    public Bot(String name) {
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
