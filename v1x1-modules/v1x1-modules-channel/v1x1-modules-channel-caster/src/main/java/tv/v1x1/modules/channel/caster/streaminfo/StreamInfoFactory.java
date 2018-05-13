package tv.v1x1.modules.channel.caster.streaminfo;


import tv.v1x1.common.dto.db.Platform;

public class StreamInfoFactory {
    public static IStreamInfo getInfo(final Platform platform) {
        switch(platform) {
            case TWITCH:
                return new TwitchStreamInfo();
            case MIXER:
            case YOUTUBE:
                throw new UnsupportedOperationException();
            default:
                throw new IllegalArgumentException(platform + " does not support streaming");
        }
    }
}
