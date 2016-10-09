package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class RplEndOfMotdCommand extends IrcStanza {
    public RplEndOfMotdCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args) {
        super(rawLine, tags, source, IrcCommand.RPL_ENDOFMOTD, rawArgs, args);
    }
}
