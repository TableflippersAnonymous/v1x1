package tv.v1x1.modules.channel.counter.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.GenericHelpMessage;
import tv.v1x1.modules.channel.counter.CounterModule;

import java.util.List;

/**
 * Created by Josh on 2019-06-25
 */
public class CounterHelpCommand extends Command {
    final private CounterModule module;
    private final CommandDelegator masterDelegator;


    public CounterHelpCommand(final CounterModule module, final CommandDelegator masterDelegator) {
        this.module = module;
        this.masterDelegator = masterDelegator;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("help");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        if(args.size() < 1)
            Chat.i18nMessage(module, channel, "help.blurb");
        Chat.message(module, channel, GenericHelpMessage.helpMessage(masterDelegator, (args.size() > 0 ? args.get(0) : ""), "!counter "));
    }

    @Override
    public String getDescription() {
        return "this!";
    }

    @Override
    public String getHelp() {
        return "Recursive help is recursive. Kappa";
    }
}

