package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;
import tv.v1x1.modules.channel.timed_messages.Timer;

import java.util.List;

/**
 * @author Josh
 */
public class TimerCreateCommand extends Command {
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
        final User commander = chatMessage.getSender();
        final long interval;
        try {
            interval = Long.getLong(args.get(0)) * 1000;
        } catch (NumberFormatException e) {
            Chat.i18nMessage(module, channel, "timer.create.badinterval");
            return;
        }
        Timer timer = new Timer(interval);
        module.createTimer(channel.getTenant(), args.get(0), timer);

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
            case 1:
                Chat.i18nMessage(module, channel, "timer.create.notarget",
                        "commander", displayName,
                        "usage", getUsage()
                );
                break;
            case 2:
                Chat.i18nMessage(module, channel, "timer.create.badinterval",
                        "commander", displayName
                );
                break;
            default:
                Chat.i18nMessage(module, channel, "timer.create.toomanyargs",
                        "commander", displayName
                );
                break;
        }

    }
}
