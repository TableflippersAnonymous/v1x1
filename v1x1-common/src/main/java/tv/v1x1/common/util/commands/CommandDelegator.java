package tv.v1x1.common.util.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.Module;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.text.WordList;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Josh
 */
public class CommandDelegator {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String prefix;
    private final CommandProvider commandProvider;
    final private Module module;

    /**
     * CommandDelegator tracks {@link Command Commands} to be run with no prefix;
     * useful for commands with many subcommands
     */
    public CommandDelegator() {
        this("", null);
    }

    @Deprecated
    public CommandDelegator(final String prefix) {
        this(new StaticCommandProvider(), prefix, null);
    }

    @Deprecated
    public CommandDelegator(final CommandProvider commandProvider, final String prefix) {
        this(commandProvider, prefix, null);
    }

    /**
     * CommandDelegator tracks {@link Command Commands} to be run
     * @param prefix The prefix this CommandDelegator looks for
     */
    public CommandDelegator(final String prefix, final Module module) {
        this(new StaticCommandProvider(), prefix, module);
    }

    /**
     * CommandDelegator tracks {@link Command Commands} to be run and has better error handling
     * @param commandProvider
     * @param prefix
     * @param module
     */
    public CommandDelegator(final CommandProvider commandProvider, final String prefix, final Module module) {
        this.prefix = prefix;
        this.commandProvider = commandProvider;
        this.module = module;
    }
    /**
     * Get the prefix for this command delegator
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Interpret chat message for commands and args, then try to execute found commands
     * @param chatMessageEvent
     */
    public void handleChatMessage(final ChatMessageEvent chatMessageEvent) {
        LOG.trace("Asked to handle chat message: {}", chatMessageEvent.getChatMessage().getText());
        final ParsedCommand parsedCmd = CommandParser.parse(chatMessageEvent.getChatMessage().getText(), prefix);
        if(parsedCmd == null)
            return;
        LOG.debug("Got parsedCommand: {}", parsedCmd.getCommand());
        handleParsedCommand(chatMessageEvent.getChatMessage(), parsedCmd);
    }

    /**
     * Semi-internal utility to check all the criteria on running a command (if it exists/has args/perms ...)
     * @param chatMessage
     * @param parsedCmd
     * @return true if we ran the command or one of its error handling functions
     * or false if we didn't find a command to run
     */
    public boolean handleParsedCommand(final ChatMessage chatMessage, final ParsedCommand parsedCmd) {
        final Command command = commandProvider.provide(parsedCmd.getCommand(), chatMessage);
        if(command == null) return false;
        LOG.debug("Found valid command: {}", parsedCmd.getCommand());
        boolean hasPerm = false;
        // Check arguments
        if((parsedCmd.getArgs().size() < command.getMinArgs()) ||
                (command.getMaxArgs() != -1 && parsedCmd.getArgs().size() > command.getMaxArgs())) {
            command.handleArgMismatch(chatMessage, parsedCmd.getCommand(), parsedCmd.getArgs());
            LOG.trace("Command had invalid args");
            return true;
        }
        // Check permissions
        final List<Permission> allowedPermissions = command.getAllowedPermissions();
        if(allowedPermissions == null) {
            hasPerm = true;
        } else {
            LOG.trace("User has these perms: ");
            for(Permission p : chatMessage.getPermissions())
                LOG.trace(p.getNode());
            for(Permission p : allowedPermissions) {
                LOG.trace("Command has " + p.getNode());
                if(chatMessage.getPermissions().contains(p)) {
                    LOG.trace("Found permission");
                    hasPerm = true;
                    break;
                }
            }
        }
        if(!hasPerm) {
            LOG.trace("No permissions");
            command.handleNoPermissions(chatMessage, parsedCmd.getCommand(), parsedCmd.getArgs());
            return true;
        }
        // Go go go
        LOG.info("Executing {} from {} in {}...", parsedCmd.getCommand(), chatMessage.getSender(), chatMessage.getChannel());
        try {
            command.run(chatMessage, parsedCmd.getCommand(), parsedCmd.getArgs());
        } catch(final Throwable throwable) {
            if(this.module != null) {
                final String errorString = WordList.randomWords(4);
                Chat.i18nMessage(module, chatMessage.getChannel(), "generic.error",
                        "commander", chatMessage.getSender().getMention(),
                        "message", throwable.getClass().getSimpleName(),
                        "errorId", errorString);
                LOG.error("BLAH! errorId={} Failed to execute command", errorString, throwable);
            }
        }
        return true;
    }

    public void registerCommand(final Command command) {
        if(commandProvider instanceof StaticCommandProvider)
            ((StaticCommandProvider)commandProvider).registerCommand(command);
        else
            throw new UnsupportedOperationException("Only StaticCommandProviders support registering commands");
    }

    public ImmutableList<Command> getRegisteredCommands() {
        if(commandProvider instanceof StaticCommandProvider)
            return ((StaticCommandProvider)commandProvider).getRegisteredCommands();
        else
            throw new UnsupportedOperationException("Only StaticCommandProviders support getRegisteredCommands()");
    }
}
