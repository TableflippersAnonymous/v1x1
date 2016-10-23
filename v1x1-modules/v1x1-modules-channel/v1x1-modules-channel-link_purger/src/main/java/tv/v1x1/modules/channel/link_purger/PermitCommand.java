package tv.v1x1.modules.channel.link_purger;

import com.google.common.collect.ImmutableList;
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
        return ImmutableList.of("permit");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("link_purger.permit"));
    }

    @Override
    public String getUsage() {
        return "permit <user>";
    }

    @Override
    public String getDescription() {
        return "Allows a user to post a link";
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
    public void run(ChatMessage chatMessage, String command, List<String> args) {
        
    }
}
