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
public class GroupInfoCommand extends Command {
    final private OpsTool opsTool;

    public GroupInfoCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("info");
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
        tenant = opsTool.getTenantByChannel(chatMessage.getChannel().getChannelGroup().getPlatform(), search);
        if(tenant == null) {
            opsTool.respond(channel, "I don't know about that channel");
            return;
        }
        TenantGroup group = opsTool.groupByName(tenant, groupName);
        if(group == null) {
            opsTool.respond(channel, "Group doesn't exist");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(group.getName());
        sb.append(" | UUID: ");
        sb.append(group.getGroupId());
        sb.append(" | Perms: ");
        if(group.getPermissions() != null) {
            boolean first = true;
            for(tv.v1x1.common.dto.db.Permission perm : group.getPermissions()) {
                if(first) first = false;
                else sb.append(", ");
                sb.append(perm.toCore());
            }
        } else {
            sb.append("<none>");
        }
        opsTool.respond(channel, sb.toString());
    }
}
