package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

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
    public void handleArgMismatch(final ChatMessage chatMessage) {
        Chat.i18nMessage(module, chatMessage.getChannel(), null, "entry.delete.notarget",
                "commander", chatMessage.getSender().getDisplayName(),
                "usage", getUsage());
    }
}
