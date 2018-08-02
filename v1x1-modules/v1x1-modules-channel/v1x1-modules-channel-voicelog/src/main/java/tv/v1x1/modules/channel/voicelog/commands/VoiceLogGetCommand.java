package tv.v1x1.modules.channel.voicelog.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.voicelog.VoiceLog;

import java.util.List;

public class VoiceLogGetCommand extends Command {
    final VoiceLog module;

    public VoiceLogGetCommand(final VoiceLog module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("get");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commanderMention = chatMessage.getSender().getMention();
        final Channel target = module.getChannel(channel);
        if(target == null) {
            Chat.i18nMessage(module, channel, "notset",
                    "commander", commanderMention);
        } else {
            Chat.i18nMessage(module, channel, "get",
                    "commander", commanderMention,
                    "target", target.getMention());
        }
    }

    @Override
    public String getDescription() {
        return "get the currently-set voice log channel";
    }

    @Override
    public int getMinArgs() {
        return 0;
    }

    @Override
    public int getMaxArgs() {
        return 0;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "invalidargs",
                "commander", chatMessage.getSender().getMention(),
                "alias", "voicelog " + command,
                "syntax", getUsage());
    }
}
