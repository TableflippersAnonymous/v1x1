package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;
import tv.v1x1.modules.channel.timed_messages.Timer;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Josh
 */
public class TimerCreateCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private TimedMessages module;

    public TimerCreateCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("create");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String senderName = chatMessage.getSender().getDisplayName();
        final long interval;
        try {
            interval = Long.parseLong(args.get(1)) * 1000;
        } catch (NumberFormatException e) {
            Chat.i18nMessage(module, channel, "create.badinterval",
                    "commander", senderName);
            return;
        }
        LOG.debug("Creating timer with interval {}", interval);
        final Timer timer = new Timer(interval);
        final String timerId = args.get(0);
        if(module.createTimer(channel.getTenant(), timerId, timer)) {
            Chat.i18nMessage(module, channel, "create.success",
                    "commander", senderName,
                    "id", timerId
            );
        } else {
            Chat.i18nMessage(module, channel, "create.alreadyexists",
                    "commander", senderName,
                    "id", timerId,
                    "cmd", "!timer add");
        }
    }

    @Override
    public String getUsage() {
        return "create <id> <time>";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String displayName = chatMessage.getSender().getDisplayName();
        switch (args.size()) {
            case 0:
                Chat.i18nMessage(module, channel, "create.notarget",
                        "commander", displayName,
                        "usage", getUsage()
                );
                break;
            case 1:
                Chat.i18nMessage(module, channel, "create.badinterval",
                        "commander", displayName
                );
                break;
            default:
                Chat.i18nMessage(module, channel, "toomanyargs",
                        "commander", displayName
                );
                break;
        }

    }
}
