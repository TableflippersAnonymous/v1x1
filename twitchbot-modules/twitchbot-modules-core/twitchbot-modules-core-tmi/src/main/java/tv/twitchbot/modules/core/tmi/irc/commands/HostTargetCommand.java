package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class HostTargetCommand extends IrcStanza {
    private String channel;
    private String targetChannel;

    public HostTargetCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String targetChannel) {
        super(rawLine, tags, source, IrcCommand.HOSTTARGET, rawArgs, args);
        this.channel = channel;
        this.targetChannel = targetChannel;
    }

    public String getChannel() {
        return channel;
    }

    public String getTargetChannel() {
        return targetChannel;
    }
}
