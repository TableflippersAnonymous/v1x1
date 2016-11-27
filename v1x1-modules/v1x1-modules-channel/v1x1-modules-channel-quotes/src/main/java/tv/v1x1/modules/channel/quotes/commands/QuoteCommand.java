package tv.v1x1.modules.channel.quotes.commands;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

/**
 * @author Josh
 */
public class QuoteCommand extends Command {
    @Override
    public List<String> getCommands() {
        return null;
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
        return super.getUsage();
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public int getMinArgs() {
        return super.getMinArgs();
    }

    @Override
    public int getMaxArgs() {
        return super.getMaxArgs();
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        super.handleArgMismatch(chatMessage, command, args);
    }
}
