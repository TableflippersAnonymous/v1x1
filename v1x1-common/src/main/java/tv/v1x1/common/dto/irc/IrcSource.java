package tv.v1x1.common.dto.irc;

import tv.v1x1.common.dto.proto.core.IRC;

/**
 * Created by naomi on 10/8/2016.
 */
public abstract class IrcSource {
    public static IrcSource fromProto(final IRC.IrcSource source) {
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
