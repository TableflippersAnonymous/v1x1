package tv.twitchbot.common.util.commands;

import java.util.List;

/**
 * Created by Josh on 2016-10-06.
 */
public class ParsedCommand {
    private final String command;
    private final List<String> args;

    ParsedCommand(final String command, final List<String> args) {
        this.command = command;
        this.args = args;
    }
    public String getCommand() { return command; }
    public List<String> getArgs() { return args; }
}
