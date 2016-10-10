package tv.twitchbot.common.dto.core;

import tv.twitchbot.common.dto.proto.core.BotOuterClass;

/**
 * Created by naomi on 10/10/2016.
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
                .setType(BotOuterClass.Bot.Type.TWITCH)
                .setExtension(BotOuterClass.TwitchBot.data, toProtoTwitchBot());
    }

    private BotOuterClass.TwitchBot toProtoTwitchBot() {
        return BotOuterClass.TwitchBot.newBuilder()
                .build();
    }
}
