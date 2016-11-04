package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;
import tv.v1x1.modules.channel.timed_messages.TimerEntry;

import java.util.List;

/**
 * @author Josh
 */
public class TimerDeleteCommand extends Command {
    private TimedMessages module;

    public TimerDeleteCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("delete");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String senderName = chatMessage.getSender().getDisplayName();
        final String timerName = args.remove(0);
        final StringBuilder message = new StringBuilder();
        for(String arg : args)
            message.append(arg);
        TimerEntry entry = module.deleteTimerEntry(channel.getTenant(), timerName, message.toString());
        if(entry == null) {
            Chat.i18nMessage(module, channel, "entry.delete.nomatch",
                    "commander", senderName,
                    "id", timerName
            );
        } else {
            Chat.i18nMessage(module, channel, "entry.delete.success",
                    "commander", senderName,
                    "id", timerName,
                    "preview", "<preview here>"
            );
        }
    }

    @Override
    public String getUsage() {
        return "delete <id> <message>";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return -1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "entry.delete.notarget",
                "commander", chatMessage.getSender().getDisplayName(),
                "usage", getUsage());
    }
}
