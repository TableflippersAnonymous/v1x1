package tv.v1x1.modules.channel.counter.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.ParsedCommand;
import tv.v1x1.modules.channel.counter.CounterModule;

import java.util.List;

/**
 * Created by Josh on 2019-06-24.
 */
public class CounterCommand extends Command {
    private final CounterModule module;
    private final CommandDelegator delegator;

    public CounterCommand(final CounterModule module) {
        this.module = module;
        this.delegator = new CommandDelegator();
        delegator.registerCommand(new CounterCreateCommand(module));
        delegator.registerCommand(new CounterDestroyCommand(module));
        delegator.registerCommand(new CounterListCommand(module));
        delegator.registerCommand(new CounterShowCommand(module));
        delegator.registerCommand(new CounterSetCommand(module));
        delegator.registerCommand(new CounterIncMsgCommand(module));
        delegator.registerCommand(new CounterDecMsgCommand(module));
        delegator.registerCommand(new CounterGeekModeCommand(module));
        delegator.registerCommand(new CounterHelpCommand(module, delegator));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("counter", "ctr");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("counter.modify"));
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "badargs",
                "commander", chatMessage.getSender().getMention(),
                "usage", getUsage());
    }

    @Override
    public void handleNoPermissions(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "generic.noperms",
                "commander", chatMessage.getSender().getMention(),
                "perm", "counter.modify");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        String subCmd = args.remove(0).toLowerCase();
        if(!delegator.handleParsedCommand(chatMessage, new ParsedCommand(subCmd, args)))
            handleArgMismatch(chatMessage, command, args);

    }
}

