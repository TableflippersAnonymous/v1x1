package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.IrcSource;
import tv.twitchbot.modules.core.tmi.irc.MessageTaggedIrcStanza;

import java.util.*;

/**
 * Created by cobi on 10/8/2016.
 */
public class PrivmsgCommand extends MessageTaggedIrcStanza {
    private String channel;
    private String message;

    private UUID id;
    private int bits;

    public PrivmsgCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args, String channel, String message) {
        super(rawLine, tags, source, IrcCommand.PRIVMSG, rawArgs, args);
        this.channel = channel;
        this.message = message;

        if(tags.containsKey("id") && !tags.get("id").isEmpty())
            id = UUID.fromString(tags.get("id"));
        if(tags.containsKey("bits") && !tags.get("bits").isEmpty())
            bits = Integer.valueOf(tags.get("bits"));
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public UUID getId() {
        return id;
    }

    public int getBits() {
        return bits;
    }
}
