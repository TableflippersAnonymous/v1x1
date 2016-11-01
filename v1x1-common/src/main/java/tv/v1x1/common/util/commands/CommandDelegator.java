package tv.v1x1.common.util.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josh
 */
public class CommandDelegator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String prefix;
    private final List<Command> registeredCommands;

    /**
     * CommandDelegator tracks {@link Command Commands} to be run with no prefix;
     * useful for commands with many subcommands
     */
    public CommandDelegator() {
        this("");
    }
    /**
     * CommandDelegator tracks {@link Command Commands} to be run
     * @param prefix The prefix this CommandDelegator looks for
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
        handleParsedCommand(chatMessageEvent.getChatMessage(), parsedCmd);
    }

    /**
     * Semi-internal utility to check all the critera on running a command
     * @param chatMessage
     * @param parsedCmd
     * @return true if we ran the command or one of its error handling functions
     * or false if we didn't find a command to run
     */
    public boolean handleParsedCommand(final ChatMessage chatMessage, final ParsedCommand parsedCmd) {
        for(final Command command : registeredCommands) {
            boolean isFound = false;
            boolean hasPerm = false;
            for(final String commandAlias : command.getCommands())
                if(parsedCmd.getCommand().equalsIgnoreCase(commandAlias))
                    isFound = true;
            if(!isFound)
                continue;
            if((parsedCmd.getArgs().size() < command.getMinArgs()) ||
                    (command.getMaxArgs() != -1 && parsedCmd.getArgs().size() > command.getMaxArgs())) {
                command.handleArgMismatch(chatMessage, parsedCmd.getCommand(), parsedCmd.getArgs());
                continue;
            }
            final List<Permission> allowedPermissions = command.getAllowedPermissions();
            if(allowedPermissions == null) {
                hasPerm = true;
            } else {
                for(Permission p : allowedPermissions) {
                    if(chatMessage.getPermissions().contains(p)) {
                        hasPerm = true;
                        break;
                    }
                }
            }
            if(!hasPerm) {
                command.handleNoPermissions(chatMessage, parsedCmd.getCommand(), parsedCmd.getArgs());
                continue;
            }
            command.run(chatMessage, parsedCmd.getCommand(), parsedCmd.getArgs());
            return true;
        }
        return false;
    }
}
