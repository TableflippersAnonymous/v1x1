package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

import java.util.List;

/**
 * @author Josh
 */
public class TimerRescheduleCommand extends Command {
    private final TimedMessages module;

    public TimerRescheduleCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("reschedule");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getMention();
        final String timerStr = args.get(0);
        final long interval;
        try {
            interval = Long.parseLong(args.get(1)) * 1000;
        } catch (NumberFormatException e) {
            Chat.i18nMessage(module, channel, "invalid.interval",
                    "commander", commander,
                    "usage", getUsage());
            return;
        }
        if(interval < (10*1000)) {
            Chat.i18nMessage(module, channel, "invalid.interval",
                    "commander", commander,
                    "usage", getUsage());
            return;
        }
        if(module.rescheduleTimer(channel.getChannelGroup().getTenant(), timerStr, interval)) {
        Chat.i18nMessage(module, channel, "reschedule.success",
                "commander", commander,
                "id", timerStr,
                "interval", interval/1000);
        } else {
            Chat.i18nMessage(module, channel, "invalid.timer",
                    "commander", commander,
                    "id", timerStr);
        }
    }

    @Override
    public String getUsage() {
        return "<timer> <time>";
    }

    @Override
    public String getDescription() {
        return "change interval of a rotation's messaging";
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
        final String commander = chatMessage.getSender().getMention();
        switch (args.size()) {
            case 0:
                Chat.i18nMessage(module, channel, "reschedule.notarget",
                        "commander", commander,
                        "usage", getUsage()
                );
                break;
            case 1:
                Chat.i18nMessage(module, channel, "invalid.interval",
                        "commander", commander,
                        "usage", getUsage()
                        );
                break;
            default:
                Chat.i18nMessage(module, channel, "toomanyargs",
                        "commander", commander,
                        "usage", getUsage()
                );
                break;
        }

    }
}
