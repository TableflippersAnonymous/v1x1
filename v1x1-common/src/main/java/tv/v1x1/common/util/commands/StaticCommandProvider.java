package tv.v1x1.common.util.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.ChatMessage;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josh
 */
public class StaticCommandProvider implements CommandProvider {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final List<Command> registeredCommands;

    public StaticCommandProvider() {
        this.registeredCommands = new ArrayList<>();
    }

    @Override
    public Command provide(final String command, final ChatMessage chatMessage) {
        for(final Command registeredCommand : registeredCommands) {
            for(final String commandAlias : registeredCommand.getCommands())
                if(command.equalsIgnoreCase(commandAlias))
                    return registeredCommand;
        }
        return null;
    }

    /**
     * Register a new command for later execution
     * @see Command
     * @param command
     */
    public void registerCommand(final Command command) {
        registeredCommands.add(command);
        LOG.debug("registered command " + command);
    }

    /**
     * Get a list of commands registered to this delegator
     * @return list of Commands
     */
    public ImmutableList<Command> getRegisteredCommands() {
        return ImmutableList.copyOf(registeredCommands);
    }
}
