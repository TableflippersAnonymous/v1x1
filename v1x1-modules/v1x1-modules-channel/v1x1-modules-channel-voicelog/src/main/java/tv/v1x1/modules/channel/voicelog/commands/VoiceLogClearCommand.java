package tv.v1x1.modules.channel.voicelog.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.voicelog.VoiceLog;

import java.util.List;

public class VoiceLogClearCommand extends Command {
    final private VoiceLog module;

    public VoiceLogClearCommand(final VoiceLog module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("clear", "unset");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commanderMention = chatMessage.getSender().getMention();
        final Channel oldChannel = module.setChannel(channel, null);
        if(oldChannel == null) {
            Chat.i18nMessage(module, channel, "notset",
                    "commander", commanderMention);
        } else {
            Chat.i18nMessage(module, channel, "clear",
                    "commander", commanderMention,
                    "oldTarget", channel.getMention());
        }
    }
}
