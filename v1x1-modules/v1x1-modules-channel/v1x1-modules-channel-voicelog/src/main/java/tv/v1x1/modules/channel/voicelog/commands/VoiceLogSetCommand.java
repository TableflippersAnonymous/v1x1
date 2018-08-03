package tv.v1x1.modules.channel.voicelog.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.voicelog.VoiceLog;

import java.util.List;

public class VoiceLogSetCommand extends Command {
    final private VoiceLog module;
    final private DisplayNameService displayNameService;

    public VoiceLogSetCommand(final VoiceLog module) {
        this.module = module;
        this.displayNameService = module.getInjector().getInstance(DisplayNameService.class);
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("set");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commanderMention = chatMessage.getSender().getMention();
        final DAOTenant daoTenant = module.getDaoManager().getDaoTenant();
        try {
            final String targetChannelId = displayNameService.getChannelIdFromMention(channel, args.get(0));
            final Channel targetChannel = daoTenant.getChannelInTenant(channel, targetChannelId);
            final Channel oldChannel = module.setChannel(channel, targetChannel);
            if(oldChannel == null) {
                Chat.i18nMessage(module, channel, "set",
                        "commander", commanderMention,
                        "target", targetChannel.getMention());
            } else if(oldChannel.equals(targetChannel)) {
                Chat.i18nMessage(module, channel, "alreadyset",
                        "commander", commanderMention,
                        "target", targetChannel.getMention());
            } else {
                Chat.i18nMessage(module, channel, "reset",
                        "commander", commanderMention,
                        "target", targetChannel.getMention(),
                        "oldtarget", oldChannel.getMention());
            }
        } catch(NoSuchTargetException|DAOTenant.NoSuchChannelException ex) {
            Chat.i18nMessage(module, channel, "targetnotfound",
                    "commander", commanderMention,
                    "target", args.get(0));
        }
    }

    @Override
    public String getUsage() {
        return "<channel>";
    }

    @Override
    public String getDescription() {
        return "set the channel to send voice logs to";
    }

    @Override
    public String getHelp() {
        return "Set a channel that gets voice connect, disconnect, and move messages sent to";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "invalidargs",
                "commander", chatMessage.getSender().getMention(),
                "alias", "voicelog " + command,
                "syntax", getUsage());
    }
}
