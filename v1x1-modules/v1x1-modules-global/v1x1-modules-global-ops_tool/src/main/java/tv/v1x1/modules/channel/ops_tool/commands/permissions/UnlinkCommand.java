package tv.v1x1.modules.channel.ops_tool.commands.permissions;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;

/**
 * @author Josh
 */
public class UnlinkCommand extends Command {
    final private OpsTool opsTool;

    public UnlinkCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("unlink");
    }

    @Override
    public String getDescription() {
        return "unlink a v1x1 group from a platform group";
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
        tenant = opsTool.getTenantByChannel(chatMessage.getChannel().getChannelGroup().getPlatform(), search);
        if(tenant == null) {
            opsTool.respond(channel, "I don't know about that channel");
            return;
        }
        if(opsTool.clearLink(channel, channel.getChannelGroup().getPlatform(), vgroup, pgroup)) {
            opsTool.respond(channel, "Platform mapping removed");
        } else {
            opsTool.respond(channel, "Group doesn't exist");
        }
    }
}
