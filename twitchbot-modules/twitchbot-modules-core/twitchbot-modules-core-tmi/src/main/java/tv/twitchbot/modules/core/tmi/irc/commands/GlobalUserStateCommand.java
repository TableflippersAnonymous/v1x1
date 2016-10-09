package tv.twitchbot.modules.core.tmi.irc.commands;

import tv.twitchbot.modules.core.tmi.irc.EmoteSetIrcStanza;
import tv.twitchbot.modules.core.tmi.irc.IrcSource;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class GlobalUserStateCommand extends EmoteSetIrcStanza {
    private int userId;

    public GlobalUserStateCommand(String rawLine, Map<String, String> tags, IrcSource source, String rawArgs, String[] args) {
        super(rawLine, tags, source, IrcCommand.GLOBALUSERSTATE, rawArgs, args);
        if(tags.containsKey("user-id") && !tags.get("user-id").isEmpty())
            userId = Integer.valueOf(tags.get("user-id"));
    }

    public int getUserId() {
        return userId;
    }
}
