package tv.v1x1.common.dto.irc;

import tv.v1x1.common.dto.proto.core.IRC;

/**
 * Created by cobi on 10/8/2016.
 */
public class IrcUser extends IrcSource {
    public static IrcUser fromProto(final IRC.IrcUser user) {
        final String nickname = user.getNickname();
        final String username = user.getUsername();
        final String hostname = user.getHostname();
        return new IrcUser(nickname, username, hostname);
    }

    private final String nickname;
    private final String username;
    private final String hostname;

    public IrcUser(final String nickname, final String username, final String hostname) {
        this.nickname = nickname;
        this.username = username;
        this.hostname = hostname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    protected IRC.IrcSource.Builder toProtoBuilder() {
        return super.toProtoBuilder()
                .setType(IRC.IrcSource.Type.USER)
                .setExtension(IRC.IrcUser.data, toProtoUser());
    }

    private IRC.IrcUser toProtoUser() {
        return IRC.IrcUser.newBuilder()
                .setNickname(nickname)
                .setUsername(username)
                .setHostname(hostname)
                .build();
    }
}
