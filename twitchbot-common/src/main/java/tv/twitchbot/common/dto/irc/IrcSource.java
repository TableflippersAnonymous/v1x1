package tv.twitchbot.common.dto.irc;

import tv.twitchbot.common.dto.proto.core.IRC;

/**
 * Created by cobi on 10/8/2016.
 */
public abstract class IrcSource {
    public static IrcSource fromProto(IRC.IrcSource source) {
        switch(source.getType()) {
            case SERVER: return IrcServer.fromProto(source.getExtension(IRC.IrcServer.data));
            case USER: return IrcUser.fromProto(source.getExtension(IRC.IrcUser.data));
            default: throw new IllegalStateException("Unknown IRC source: " + source.getType());
        }
    }

    public IRC.IrcSource toProto() {
        return toProtoBuilder().build();
    }

    protected IRC.IrcSource.Builder toProtoBuilder() {
        return IRC.IrcSource.newBuilder();
    }
}
