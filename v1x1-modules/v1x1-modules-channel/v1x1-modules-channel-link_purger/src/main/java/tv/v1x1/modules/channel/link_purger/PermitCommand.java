package tv.v1x1.modules.channel.link_purger;

import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

/**
 * @author Josh
 */
public class PermitCommand extends Command {
    private final LinkPurger module;
    public PermitCommand(LinkPurger linkPurger) {
        module = linkPurger;
    }

    @Override
    public List<String> getCommands() {
        return null;
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(ChatMessage chatMessage, String command, List<String> args) {

    }
}
