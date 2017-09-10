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
public class GroupListCommand extends Command {
    final private OpsTool opsTool;

    public GroupListCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("list");
    }

    @Override
    public String getDescription() {
        return "list groups for tenant";
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getUsage() {
        return "<channel>";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Tenant tenant;
        final String search = args.get(0);
        final Channel channel = chatMessage.getChannel();
        tenant = opsTool.getTenantByChannel(chatMessage.getChannel().getChannelGroup().getPlatform(), search);
        if(tenant == null) {
            opsTool.respond(channel, "I don't know about that channel");
        }
        final Iterable<TenantGroup> groups = opsTool.getGroups(tenant);
        final StringBuilder sb = new StringBuilder();
        sb.append("List of groups: ");
        for(TenantGroup group : groups) {
            sb.append(group.getName());
            sb.append(' ');
        }
        opsTool.respond(channel, sb.toString());
    }
}
