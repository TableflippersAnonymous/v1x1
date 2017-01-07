package tv.v1x1.modules.channel.ops_tool.commands.permissions;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;

/**
 * @author Josh
 */
public class GroupCreateCommand extends Command {
    final private OpsTool opsTool;

    public GroupCreateCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("create");
    }

    @Override
    public String getDescription() {
        return "create a group for tenant";
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getUsage() {
        return "<channel> <vgroup>";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Tenant tenant;
        final String search = args.get(0);
        final String groupName = args.get(1);
        final Channel channel = chatMessage.getChannel();
        tenant = opsTool.getTenantByChannel(chatMessage.getChannel().getPlatform(), search);
        if(tenant == null) {
            opsTool.respond(channel, "I don't know about that channel");
        }
        opsTool.createGroup(tenant, groupName);
        opsTool.respond(channel, "Created group");
    }
}
