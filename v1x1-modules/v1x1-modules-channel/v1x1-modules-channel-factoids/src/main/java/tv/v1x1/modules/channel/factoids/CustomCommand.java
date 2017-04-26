package tv.v1x1.modules.channel.factoids;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

/**
 * @author Josh
 */
public class CustomCommand extends Command {
    private final String cmd;
    private final String response;
    private final Permission permission;
    private final FactoidsModule module;

    public CustomCommand(final FactoidsModule module, final String cmd, final String response, final tv.v1x1.common.dto.db.Permission permission) {
        this.cmd = cmd.toLowerCase();
        this.response = response;
        if(permission != null)
            this.permission = permission.toCore();
        else
            this.permission = null;
        this.module = module;
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        if(permission == null) return null;
        else return ImmutableList.of(permission, new Permission("factoids.useall"));
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of(cmd);
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        if(args.size() > 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append(args.get(0));
            sb.append(": ");
            sb.append(response);
            Chat.message(module, chatMessage.getChannel(), sb.toString());
        }
        else {
            Chat.message(module, chatMessage.getChannel(), response);
        }
    }
}
