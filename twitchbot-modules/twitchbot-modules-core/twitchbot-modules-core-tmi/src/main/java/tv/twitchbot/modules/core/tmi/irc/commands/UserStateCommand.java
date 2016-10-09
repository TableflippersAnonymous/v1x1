package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.EmoteSetIrcStanza;
import tv.twitchbot.modules.core.tmi.irc.IrcSource;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class UserStateCommand extends EmoteSetIrcStanza {
    private String channel;

    public UserStateCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel) {
        super(rawLine, tags, source, IrcCommand.USERSTATE, rawArgs, args);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
