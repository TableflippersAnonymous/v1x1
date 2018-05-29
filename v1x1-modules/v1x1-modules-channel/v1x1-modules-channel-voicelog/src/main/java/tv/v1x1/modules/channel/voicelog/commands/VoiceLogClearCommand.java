package tv.v1x1.modules.channel.voicelog.commands;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.util.commands.Command;

import java.util.List;

public class VoiceLogClearCommand extends Command {
    @Override
    public List<String> getCommands() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commanderMention = chatMessage.getSender().getMention();
    }
}
