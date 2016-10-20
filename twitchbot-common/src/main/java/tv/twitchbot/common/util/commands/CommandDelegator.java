package tv.twitchbot.common.util.commands;

import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josh on 2016-10-06.
 */
public class CommandDelegator {
    private String prefix;
    private List<Command> registeredCommands;

    /**
     * CommandDelegator tracks {@link Command Commands} to be run
     * @param prefix
     */
    public CommandDelegator(String prefix) {
        this.prefix = prefix;
        registeredCommands = new ArrayList<>();
    }

    /**
     * Register a new command for later execution
     * @see Command
     * @param command
     */
    public void registerCommand(Command command) {
        registeredCommands.add(command);
    }

    /**
     * Interpret chat message for commands and args, then execute found commands
     * @param chatMessageEvent
     */
    public void handleChatMessage(ChatMessageEvent chatMessageEvent) {
        ParsedCommand parsedCmd = CommandParser.parse(chatMessageEvent, prefix);
        if(parsedCmd == null)
            return;
        System.out.println("Got parsedCommand: " + parsedCmd.getCommand());
        for(Command command : registeredCommands) {
            boolean isFound = false;
            for(String commandAlias : command.getCommands())
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
