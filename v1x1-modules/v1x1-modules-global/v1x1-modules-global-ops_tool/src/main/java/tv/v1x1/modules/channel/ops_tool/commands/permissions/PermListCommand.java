package tv.v1x1.modules.channel.ops_tool.commands.permissions;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Josh
 */
public class PermListCommand extends Command {
    final private OpsTool opsTool;

    public PermListCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("lsperm", "lsperms");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public String getUsage() {
        return "<channel> <group>";
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
        final Set<Permission> perms = opsTool.getPerms(tenant, groupName);
        opsTool.respond(channel, "Perms: " + String.join(", ", perms.stream().map(Permission::getNode).collect(Collectors.toSet())));
    }
}
