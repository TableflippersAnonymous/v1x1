package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Permission;
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
        final Channel channel = chatMessage.getChannel();
        if(args.size() > 0) {
            final String search = args.get(0);
            final GlobalUser user = opsTool.getUser(channel.getPlatform(), search);
            if(user == null)
                opsTool.respond(channel, "I don't know that user");
            else
                opsTool.respond(channel, "GlobalUser for " + search + ": " + user);
        } else {
            opsTool.respond(channel, "GlobalUser for " + chatMessage.getSender().getId() + ": " + chatMessage.getSender().getGlobalUser());
        }
    }
}
