package tv.twitchbot.modules.core.tmi.irc;

/**
 * Created by cobi on 10/8/2016.
 */
public class IrcServer extends IrcSource {
    private String name;

    public IrcServer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
