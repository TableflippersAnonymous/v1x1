package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.util.List;

/**
 * @author Josh
 */
public class OpsToolUserIdCommand extends Command {
    final private OpsTool opsTool;

    public OpsToolUserIdCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("userId");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public String getUsage() {
        return "[id]";
    }

    @Override
    public String getDescription() {
        return "get the GlobalUser of a user";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final DisplayNameService displayNameService = opsTool.getInjector().getInstance(DisplayNameService.class);
        final Channel channel = chatMessage.getChannel();
        try {
            final String userId;
            if (args.size() > 0)
                userId = displayNameService.getIdFromDisplayName(channel, args.get(0));
            else
                userId = chatMessage.getSender().getId();
            final GlobalUser user = opsTool.getUser(channel.getChannelGroup().getPlatform(), userId);
            if (user == null)
                opsTool.respond(channel, "I don't know that user");
            else
                opsTool.respond(channel, "GlobalUser for " + displayNameService.getDisplayNameFromId(channel, userId) + "/" + userId + ": " + user);
        } catch (final NoSuchUserException e) {
            opsTool.respond(channel, "I don't know that user");
        }
    }
}
