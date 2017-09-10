package tv.v1x1.common.dto.db;

import tv.v1x1.common.dto.proto.core.PlatformOuterClass;

/**
 * Created by cobi on 10/16/2016.
 */
public enum Platform {
    TWITCH(PlatformOuterClass.Platform.TWITCH), DISCORD(PlatformOuterClass.Platform.DISCORD),
    API(PlatformOuterClass.Platform.API), SLACK(PlatformOuterClass.Platform.SLACK),
    MIXER(PlatformOuterClass.Platform.MIXER), YOUTUBE(PlatformOuterClass.Platform.YOUTUBE),
    CURSE(PlatformOuterClass.Platform.CURSE);

    private final PlatformOuterClass.Platform protobuf;

    Platform(final PlatformOuterClass.Platform protobuf) {
        this.protobuf = protobuf;
    }

    public PlatformOuterClass.Platform toProto() {
        return protobuf;
    }
}
