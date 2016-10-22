package tv.v1x1.common.dto.irc;

import tv.v1x1.common.dto.proto.core.IRC;

/**
 * Created by cobi on 10/8/2016.
 */
public class IrcServer extends IrcSource {
    public static IrcServer fromProto(final IRC.IrcServer server) {
        final String name = server.getName();
        return new IrcServer(name);
    }

    private final String name;

    public IrcServer(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    protected IRC.IrcSource.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setType(IRC.IrcSource.Type.SERVER)
                .setExtension(IRC.IrcServer.data, toProtoServer());
    }

    private IRC.IrcServer toProtoServer() {
        return IRC.IrcServer.newBuilder()
                .setName(name)
                .build();
    }
}
