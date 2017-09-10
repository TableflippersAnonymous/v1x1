package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

import java.util.List;
import java.util.Set;

/**
 * @author Josh
 */
/* pkg-private */ class TimerListCommand extends Command {
    private TimedMessages module;

    TimerListCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("list");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String senderName = chatMessage.getSender().getDisplayName();
        final Set<String> timers = module.listTimers(channel.getChannelGroup().getTenant());
        if(timers.size() < 1) {
            Chat.i18nMessage(module, channel, "list.empty",
                    "commander", senderName);
        } else {
            Chat.i18nMessage(module, channel, "list.success",
                    "commander", senderName,
                    "timers", String.join(", ", timers)
            );
        }
    }

    @Override
    public String getUsage() {
        return "<no args>";
    }

    @Override
    public String getDescription() {
        return "list all rotations";
    }
}
