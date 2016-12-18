package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.text.Shorten;
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
        final String message = String.join(" ", args);
        final int matches = module.countMatchingTimerEntries(channel.getTenant(), timerName, message);
        if(matches == -1) {
            Chat.i18nMessage(module, channel, "invalid.timer",
                    "commander", senderName,
                    "id", timerName);
            return;
        } else if(matches > 1) {
            Chat.i18nMessage(module, channel, "delete.toomanymatches",
                    "commander", senderName,
                    "preview", Shorten.genPreview(message),
                    "count", matches,
                    "id", timerName);
            return;
        }
        TimerEntry entry = module.deleteTimerEntry(channel.getTenant(), timerName, message);
        if(entry == null) {
            Chat.i18nMessage(module, channel, "delete.nomatch",
                    "commander", senderName,
                    "id", timerName,
                    "preview", Shorten.genPreview(message)
            );
        } else {
            Chat.i18nMessage(module, channel, "delete.success",
                    "commander", senderName,
                    "id", timerName,
                    "preview", Shorten.genPreview(entry.getMessage(), 128)
            );
        }
    }

    @Override
    public String getUsage() {
        return "<timer> <message>";
    }

    @Override
    public String getDescription() {
        return "remove an entry from a rotation";
    }

    @Override
    public String getHelp() {
        return "You don't need to type the entire message, the first part of it";
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
        switch(args.size()) {
            case 0: Chat.i18nMessage(module, chatMessage.getChannel(), "delete.notarget",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "usage", getUsage()
                );
                break;
            case 1: Chat.i18nMessage(module, chatMessage.getChannel(), "delete.nomessage",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "usage", getUsage()
                );
                break;
        }
    }
}
