package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.common.util.commands.GenericHelpMessage;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Josh
 */
public class TimerHelpCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private TimedMessages module;
    private CommandDelegator masterDelegator;

    public TimerHelpCommand(final TimedMessages module, final CommandDelegator delegator) {
        this.module = module;
        this.masterDelegator = delegator;
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
        Chat.message(module, channel, GenericHelpMessage.helpMessage(masterDelegator, (args.size() > 0 ? args.get(0) : ""), "!timer "));
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
