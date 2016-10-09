package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.Map;

/**
 * Created by naomi on 10/9/2016.
 */
public class PingCommand extends IrcStanza {
    private String token;

    public PingCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String token) {
        super(rawLine, tags, source, IrcCommand.PING, rawArgs, args);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
