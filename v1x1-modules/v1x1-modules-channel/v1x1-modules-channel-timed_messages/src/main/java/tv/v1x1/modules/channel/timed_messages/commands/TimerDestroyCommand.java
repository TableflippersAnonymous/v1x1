package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;

import java.util.List;

/**
 * @author Josh
 */
/* pkg-private */ class TimerDestroyCommand extends Command {
    private TimedMessages module;

    /* pkg-private */ TimerDestroyCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("destroy");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String senderName = chatMessage.getSender().getDisplayName();
        if(module.destroyTimer(channel.getChannelGroup().getTenant(), args.get(0))) {
            Chat.i18nMessage(module, channel, "destroy.success",
                    "commander", senderName,
                    "id", args.get(0)
                    );
        } else {
            Chat.i18nMessage(module, channel, "invalid.timer",
                    "commander", senderName,
                    "id", args.get(0)
            );
        }
    }

    @Override
    public String getUsage() {
        return "<timer>";
    }

    @Override
    public String getDescription() {
        return "destroy a timer rotation";
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
        switch(args.size()) {
            case 0: Chat.i18nMessage(module, chatMessage.getChannel(), "destroy.notarget",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "usage", getUsage());
                break;
            default:
                Chat.i18nMessage(module, chatMessage.getChannel(), "toomanyargs",
                        "commander", chatMessage.getSender().getDisplayName(),
                        "usage", getUsage()
                );
        }
    }
}
