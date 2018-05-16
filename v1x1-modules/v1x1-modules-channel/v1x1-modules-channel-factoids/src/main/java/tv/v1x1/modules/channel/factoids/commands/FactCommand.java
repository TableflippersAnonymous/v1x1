package tv.v1x1.modules.channel.factoids.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.ParsedCommand;
import tv.v1x1.modules.channel.factoids.FactoidsModule;

import java.util.List;

/**
 * @author Josh
 */
public class FactCommand extends Command {
    private final FactoidsModule module;
    private final CommandDelegator delegator;

    public FactCommand(FactoidsModule module) {
        this.module = module;
        delegator = new CommandDelegator();
        delegator.registerCommand(new FactAddCommand(module));
        delegator.registerCommand(new FactEnableCommand(module));
        delegator.registerCommand(new FactDisableCommand(module));
        delegator.registerCommand(new FactEditCommand(module));
        delegator.registerCommand(new FactInfoCommand(module));
        delegator.registerCommand(new FactListCommand(module));
        delegator.registerCommand(new FactSetpermCommand(module));
        delegator.registerCommand(new FactAliasCommand(module));
        delegator.registerCommand(new FactRemoveCommand(module));
        delegator.registerCommand(new FactHelpCommand(module, delegator));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("fact");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("fact.modify"));
    }

    @Override
    public String getDescription() {
        return "Modify factoids";
    }

    @Override
    public String getUsage() {
        return "<command> [args]";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return -1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "invalid.subcommand",
                "commander", chatMessage.getSender().getMention(),
                "usage", getUsage()
        );
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        String subCmd = args.remove(0).toLowerCase();
        if(!delegator.handleParsedCommand(chatMessage, new ParsedCommand(subCmd, args)))
            handleArgMismatch(chatMessage, command, args);
    }
}
