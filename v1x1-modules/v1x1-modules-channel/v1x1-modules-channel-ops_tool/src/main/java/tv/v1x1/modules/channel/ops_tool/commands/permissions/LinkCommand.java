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
public class LinkCommand extends Command {
    final private OpsTool opsTool;

    public LinkCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("link");
    }

    @Override
    public String getDescription() {
        return "link a v1x1 group to a platform group";
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getUsage() {
        return "<channel> <vgroup> <pgroup>";
    }

    @Override
    public int getMinArgs() {
        return 3;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Tenant tenant;
        final String search = args.get(0);
        final String vgroup = args.get(1);
        final String pgroup = args.get(2);
        final Channel channel = chatMessage.getChannel();
        tenant = opsTool.getTenantByChannel(chatMessage.getChannel().getPlatform(), search);
        if(tenant == null) {
            opsTool.respond(channel, "I don't know about that channel");
            return;
        }
        if(opsTool.linkGroup(channel, channel.getPlatform(), vgroup, pgroup)) {
            opsTool.respond(channel, "Platform mapping created");
        } else {
            opsTool.respond(channel, "Group doesn't exist");
        }
    }
}
