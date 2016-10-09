package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.IrcStanza;

import java.util.Map;

/**
 * Created by cobi on 10/8/2016.
 */
public class ClearChatCommand extends IrcStanza {
    private String channel;
    private String nickname;

    private int banDuration;
    private String banReason;

    public ClearChatCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String nickname) {
        super(rawLine, tags, source, IrcCommand.CLEARCHAT, rawArgs, args);
        this.channel = channel;
        this.nickname = nickname;
        if(tags.containsKey("ban-duration") && !tags.get("ban-duration").isEmpty())
            banDuration = Integer.valueOf(tags.get("ban-duration"));
        if(tags.containsKey("ban-reason") && !tags.get("ban-reason").isEmpty())
            banReason = tags.get("ban-reason");
    }

    public String getChannel() {
        return channel;
    }

    public String getNickname() {
        return nickname;
    }

    public int getBanDuration() {
        return banDuration;
    }

    public String getBanReason() {
        return banReason;
    }
}
