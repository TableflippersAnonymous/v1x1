package tv.twitchbot.common.dto.irc;

import tv.twitchbot.common.dto.proto.core.IRC;

/**
 * Created by naomi on 10/8/2016.
 */
public class IrcUser extends IrcSource {
    public static IrcUser fromProto(IRC.IrcUser user) {
        String nickname = user.getNickname();
        String username = user.getUsername();
        String hostname = user.getHostname();
        return new IrcUser(nickname, username, hostname);
    }

    private String nickname;
    private String username;
    private String hostname;

    public IrcUser(String nickname, String username, String hostname) {
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
