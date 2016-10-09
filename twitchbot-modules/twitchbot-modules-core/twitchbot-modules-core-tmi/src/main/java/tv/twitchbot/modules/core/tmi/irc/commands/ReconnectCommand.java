package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class ReconnectCommand extends IrcStanza {
    public ReconnectCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args) {
        super(rawLine, tags, source, IrcCommand.RECONNECT, rawArgs, args);
    }
}
