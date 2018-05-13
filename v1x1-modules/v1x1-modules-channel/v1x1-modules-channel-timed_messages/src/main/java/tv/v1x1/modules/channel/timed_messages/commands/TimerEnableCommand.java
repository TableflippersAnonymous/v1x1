package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;
import tv.v1x1.modules.channel.timed_messages.Timer;

import java.util.List;

/**
 * @author Josh
 */
public class TimerEnableCommand extends Command {
    private final TimedMessages module;

    public TimerEnableCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("enable", "disable");
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
        final Timer t = module.getTimer(channel.getChannelGroup().getTenant(), timerStr);
        if(t == null) {
            Chat.i18nMessage(module, channel, "invalid.timer");
            return;
        }
        final boolean enabled;
        enabled = command.equals("enable");
        try {
            if(module.enableTimer(channel.getChannelGroup().getTenant(), timerStr, enabled)) {
                Chat.i18nMessage(module, channel, command + ".success",
                        "commander", commander,
                        "id", timerStr);
            } else {
                Chat.i18nMessage(module, channel, "alreadytoggled",
                        "commander", commander,
                        "id", timerStr,
                        "state", (enabled ? "enabled" : "disabled"));
            }
        } catch (IllegalStateException e) {
            Chat.i18nMessage(module, channel, "generic.error",
                    "message", e.getMessage());
        }
    }

    @Override
    public String getUsage() {
        return "<timer>";
    }

    @Override
    public String getDescription() {
        return "toggle a rotation without destroying it";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getMention();
        switch (args.size()) {
            case 0:
                Chat.i18nMessage(module, channel, command + ".notarget",
                        "commander", commander,
                        "usage", getUsage()
                );
                break;
            default:
                Chat.i18nMessage(module, channel, "toomanyargs",
                        "commander", commander
                );
                break;
        }

    }
}
