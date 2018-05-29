package tv.v1x1.modules.channel.ops_tool.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.util.GoGetter;
import tv.v1x1.common.util.NoSuchThingException;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.ops_tool.OpsTool;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class OpsToolGetChannelCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    final private OpsTool opsTool;

    OpsToolGetChannelCommand(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("getChannel");
    }

    @Override
    public String getUsage() {
        return "<mention>";
    }

    @Override
    public String getDescription() {
        return "try to fetch a User from their mention";
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final DisplayNameService displayNameService = opsTool.getDisplayNameService();
        final Channel channel = chatMessage.getChannel();
        String mention = channel.getMention();
        if(args.size() > 0)
            mention = args.get(0);
        try {
            LOG.debug("Looking for " + mention);
            final String channelId = displayNameService.getChannelIdFromMention(channel, mention);
            LOG.debug("Found channel ID: " + channelId);
            final Channel targetChannel = GoGetter.getMeAChannel(opsTool.getDaoManager().getDaoTenant(),
                    channel.getPlatform(), channelId);
            opsTool.respond(channel, "Channel info for " + mention + ": " + targetChannel +
                    (!targetChannel.getTenant().equals(channel.getTenant()) ? ". Target not in same Tenant!" : ""));
        } catch(NoSuchTargetException|NoSuchThingException e) {
            opsTool.respond(channel, "Target not found. " + e.getMessage());
        }
    }
}
