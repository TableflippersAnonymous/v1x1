package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.ParsedCommand;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

import java.util.List;

/**
 * @author Josh
 */
public class TimerCommand extends Command {
    private final TimedMessages module;
    private final CommandDelegator delegator;

    public TimerCommand(TimedMessages module) {
        this.module = module;
        delegator = new CommandDelegator();
        delegator.registerCommand(new TimerCreateCommand(module));
        delegator.registerCommand(new TimerDestroyCommand(module));
        delegator.registerCommand(new TimerEnableCommand(module));
        delegator.registerCommand(new TimerRescheduleCommand(module));
        delegator.registerCommand(new TimerListCommand(module));
        delegator.registerCommand(new TimerInfoCommand(module));
        delegator.registerCommand(new TimerAddCommand(module));
        delegator.registerCommand(new TimerRemoveCommand(module));
        delegator.registerCommand(new TimerHelpCommand(module, delegator));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("timer", "timers");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("timer.modify"));
    }

    @Override
    public String getDescription() {
        return "Modify timers";
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
        Chat.i18nMessage(module, chatMessage.getChannel(), "help");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        String subCmd = args.remove(0).toLowerCase();
        if(!delegator.handleParsedCommand(chatMessage, new ParsedCommand(subCmd, args)))
            handleArgMismatch(chatMessage, command, args);
    }
}
