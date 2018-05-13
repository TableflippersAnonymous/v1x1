package tv.v1x1.common.dto.db;

import tv.v1x1.common.dto.proto.core.PlatformOuterClass;
import tv.v1x1.common.util.text.CaseChanger;

/**
 * Created by naomi on 10/16/2016.
 */
public enum Platform {
    TWITCH(PlatformOuterClass.Platform.TWITCH, "Twitch"),
    DISCORD(PlatformOuterClass.Platform.DISCORD, "Discord"),
    API(PlatformOuterClass.Platform.API, "API"),
    SLACK(PlatformOuterClass.Platform.SLACK, "Slack"),
    MIXER(PlatformOuterClass.Platform.MIXER, "Mixer"),
    YOUTUBE(PlatformOuterClass.Platform.YOUTUBE, "YouTube"),
    CURSE(PlatformOuterClass.Platform.CURSE, "Curse");

    private final PlatformOuterClass.Platform protobuf;
    private final String stylizedName;

    Platform(final PlatformOuterClass.Platform protobuf, final String stylizedName) {
        this.protobuf = protobuf;
        this.stylizedName = stylizedName;
    }

    public PlatformOuterClass.Platform toProto() {
        return protobuf;
    }

    public String stylize() {
        return stylizedName;
    }
}
