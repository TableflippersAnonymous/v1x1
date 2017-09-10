package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchUserException;
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
        final DisplayNameService displayNameService = opsTool.getInjector().getInstance(DisplayNameService.class);
        final String channelId;
        try {
            if (args.size() > 0)
                channelId = displayNameService.getIdFromDisplayName(chatMessage.getChannel(), args.get(0));
            else
                channelId = chatMessage.getChannel().getId();
            final Tenant t = opsTool.getTenantByChannel(chatMessage.getChannel().getChannelGroup().getPlatform(), channelId);
            if (t == null)
                opsTool.respond(chatMessage.getChannel(), "I don't know about that channel");
            else
                opsTool.respond(chatMessage.getChannel(), "Tenant for " + displayNameService.getDisplayNameFromId(chatMessage.getChannel(), channelId) + "/" + channelId + ": " + t.toString());
        } catch (final NoSuchUserException e) {
            opsTool.respond(chatMessage.getChannel(), "I don't know about that channel");
        }
    }
}
