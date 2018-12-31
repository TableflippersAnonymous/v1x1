package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;

/**
 * @author Josh
 */
public class OpsToolTenantIdCommand extends Command {
    final private OpsTool opsTool;

    public OpsToolTenantIdCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("tenantId");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getUsage() {
        return "[channel]";
    }

    @Override
    public String getDescription() {
        return "get tenant ID of channel";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Tenant t = chatMessage.getChannel().getTenant();
        if(t == null) {
            opsTool.respond(chatMessage.getChannel(), "I don't know about that channel");
        } else {
            opsTool.respond(chatMessage.getChannel(), chatMessage.getChannel().getMention() + " is part of " +
                    t.getDisplayName() + ", tenant ID: " +
                    t.getId());
        }
    }
}
