package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

import java.util.List;

/**
 * @author Josh
 */
public class TimerAddCommand extends Command {
    private TimedMessages module;

    public TimerAddCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("add");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage) {
        Chat.i18nMessage(module, chatMessage.getChannel(), null, "invalid.add.notarget",
                "commander", chatMessage.getSender().getDisplayName(),
                "usage", getUsage()
                );
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {

    }

    @Override
    public String getUsage() {
        return "add <id> <message>";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return -1;
    }
}
