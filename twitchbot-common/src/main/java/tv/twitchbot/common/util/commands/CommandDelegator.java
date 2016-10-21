package tv.twitchbot.common.util.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 2016-10-06.
 */
public class CommandDelegator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String prefix;
    private final List<Command> registeredCommands;

    /**
     * CommandDelegator tracks {@link Command Commands} to be run
     * @param prefix
     */
    public CommandDelegator(final String prefix) {
        this.prefix = prefix;
        registeredCommands = new ArrayList<>();
    }

    /**
     * Register a new command for later execution
     * @see Command
     * @param command
     */
    public void registerCommand(final Command command) {
        registeredCommands.add(command);
    }

    /**
     * Interpret chat message for commands and args, then execute found commands
     * @param chatMessageEvent
     */
    public void handleChatMessage(final ChatMessageEvent chatMessageEvent) {
        final ParsedCommand parsedCmd = CommandParser.parse(chatMessageEvent, prefix);
        if(parsedCmd == null)
            return;
        LOG.debug("Got parsedCommand: {}", parsedCmd.getCommand());
        for(final Command command : registeredCommands) {
            boolean isFound = false;
            for(final String commandAlias : command.getCommands())
                if(parsedCmd.getCommand().equalsIgnoreCase(commandAlias))
                    isFound = true;
            if(!isFound)
                continue;
            if(parsedCmd.getArgs().size() < command.getMinArgs())
                continue;
            if(command.getMaxArgs() != -1 && parsedCmd.getArgs().size() > command.getMaxArgs())
                continue;
            command.run(chatMessageEvent.getChatMessage(), parsedCmd.getCommand(), parsedCmd.getArgs());
        }
    }
}
