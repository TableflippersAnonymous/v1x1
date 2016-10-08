package tv.twitchbot.modules.core.tmi;

import java.util.Map;

/**
 * Created by naomi on 10/8/2016.
 */
public class IrcStanza {
    public enum IrcCommand {
        JOIN, PART, PRIVMSG,
        NAMES, MODE, NOTICE,
        HOSTTARGET, CLEARCHAT,
        USERSTATE, RECONNECT,
        ROOMSTATE, USERNOTICE,
        GLOBALUSERSTATE;
    }

    private String rawLine;
    private Map<String, String> tags;
    private IrcSource source;
    private IrcCommand command;
    private String[] rawArgs;

    public IrcStanza(String rawLine, Map<String, String> tags, IrcSource source, IrcCommand command, String[] rawArgs) {
        this.rawLine = rawLine;
        this.tags = tags;
        this.source = source;
        this.command = command;
        this.rawArgs = rawArgs;
    }

    public String getRawLine() {
        return rawLine;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public IrcSource getSource() {
        return source;
    }

    public IrcCommand getCommand() {
        return command;
    }

    public String[] getRawArgs() {
        return rawArgs;
    }
}
