package tv.v1x1.modules.channel.voicelog.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.voicelog.VoiceLog;
import tv.v1x1.modules.channel.voicelog.VoiceLogUserConfiguration;

import java.util.List;

public class VoiceLogCommand extends Command {
    private final VoiceLog module;

    public VoiceLogCommand(final VoiceLog module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("voicelog");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return ImmutableList.of(new Permission("voicelog.set"));
    }

    @Override
    public String getUsage() {
        return "<set|get|clear> [channel]";
    }

    @Override
    public String getDescription() {
        return "get/set channel getting messages";
    }

    @Override
    public String getHelp() {
        return "See or change the channel receiving voice join/part messages";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        Chat.i18nMessage(module, chatMessage.getChannel(), "invalidargs",
                "commander", chatMessage.getSender().getMention(),
                "alias", command,
                "syntax", getUsage());
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final String subCmd = args.get(0).toLowerCase();
        final Channel channel = chatMessage.getChannel();
        final String commanderMention = chatMessage.getSender().getMention();
        if(subCmd.equals("set")) {

        } else {
            if(subCmd.equals("get")) {

            } else {
                Chat.i18nMessage(module, channel, "invalidsubcmd",
                "commander", commanderMention,
                        "subcmd", subCmd,
                        "syntax", getUsage());
            }
        }
    }
}
