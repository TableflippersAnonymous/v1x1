package tv.twitchbot.modules.core.tmi.irc;

/**
 * Created by cobi on 10/8/2016.
 */
public class IrcUser extends IrcSource {
    private String nickname;
    private String username;
    private String hostname;

    public IrcUser(String nickname, String username, String hostname) {
        this.nickname = nickname;
        this.username = username;
        this.hostname = hostname;
    }
}
