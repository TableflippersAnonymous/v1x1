package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class PartCommand extends IrcStanza {
    private String channel;

    public PartCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel) {
        super(rawLine, tags, source, IrcCommand.PART, rawArgs, args);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
