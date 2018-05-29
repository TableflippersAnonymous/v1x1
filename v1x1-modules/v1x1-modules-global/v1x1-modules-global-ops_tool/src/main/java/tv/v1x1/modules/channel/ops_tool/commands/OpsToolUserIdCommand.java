package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.*;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.util.GoGetter;
import tv.v1x1.common.util.NoSuchThingException;
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
        return ImmutableList.of("userId", "getUser");
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
        final DisplayNameService displayNameService = opsTool.getDisplayNameService();
        final Channel channel = chatMessage.getChannel();
        String mention = chatMessage.getSender().getMention();
        if(args.size() > 0)
            mention = args.get(0);
        try {
            final String userId = displayNameService.getUserIdFromMention(channel, mention);
            final User user = GoGetter.getMeAUser(opsTool.getDaoManager().getDaoGlobalUser(), channel.getPlatform(), userId);
            opsTool.respond(channel, "GlobalUser for " + mention + ": " + user);
        } catch(NoSuchTargetException|NoSuchThingException e) {
            opsTool.respond(channel, "Target not found. " + e.getMessage());
        }
    }
}