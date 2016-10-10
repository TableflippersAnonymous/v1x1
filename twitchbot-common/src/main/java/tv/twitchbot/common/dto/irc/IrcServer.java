package tv.twitchbot.common.dto.irc;

import tv.twitchbot.common.dto.proto.core.IRC;

/**
 * Created by cobi on 10/8/2016.
 */
public class IrcServer extends IrcSource {
    public static IrcServer fromProto(IRC.IrcServer server) {
        String name = server.getName();
        return new IrcServer(name);
    }

    private String name;

    public IrcServer(String name) {
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
