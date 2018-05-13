package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;

/**
 * @author Josh
 */
public class OpsToolInsmodCommand extends Command {
    private final OpsTool opsTool;
    public OpsToolInsmodCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("insmod");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getUsage() {
        return "<channel> <module>";
    }

    @Override
    public String getDescription() {
        return "enable a module for a tenant";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        /*Tenant t = opsTool.getTenantByChannel(chatMessage.getChannel().getPlatform(), args.get(0));
        if(t == null) {
            opsTool.respond(chatMessage.getChannel(), "Channel not found");
            return;
        }
        TenantConfiguration config = opsTool.getTenantConfiguration(args.get(1), chatMessage.getChannel());
        config.
        */
    }
}
