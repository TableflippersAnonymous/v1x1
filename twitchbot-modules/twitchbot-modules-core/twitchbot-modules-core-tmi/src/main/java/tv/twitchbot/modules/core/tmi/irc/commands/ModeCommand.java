package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.List;
import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class ModeCommand extends IrcStanza {
    private String channel;
    private String modeString;
    private List<String> nicknames;

    public ModeCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String modeString, List<String> nicknames) {
        super(rawLine, tags, source, IrcCommand.MODE, rawArgs, args);
        this.channel = channel;
        this.modeString = modeString;
        this.nicknames = nicknames;
    }

    public String getChannel() {
        return channel;
    }

    public String getModeString() {
        return modeString;
    }

    public List<String> getNicknames() {
        return nicknames;
    }
}
