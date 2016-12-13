package tv.v1x1.modules.channel.timed_messages.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.text.Shorten;
import tv.v1x1.modules.channel.timed_messages.TimedMessages;
import tv.v1x1.modules.channel.timed_messages.Timer;
import tv.v1x1.modules.channel.timed_messages.TimerEntry;

import java.util.List;
import java.util.Set;

/**
 * @author Josh
 */
/* pkg-private */ class TimerInfoCommand extends Command {
    private TimedMessages module;

    TimerInfoCommand(final TimedMessages module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("info");
    }

    @Override
    public List<Permission> getAllowedPermissions() {
        return null;
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String senderName = chatMessage.getSender().getDisplayName();
        final String timerStr = args.get(0);
        final Timer t = module.getTimer(channel.getTenant(), timerStr);
        if(t == null) {
            Chat.i18nMessage(module, channel, "invalid.timer",
                    "commander", senderName,
            "id", timerStr);
        } else {
            final StringBuilder sb = new StringBuilder();
            if(t.getEntries().size() < 1) {
                Chat.i18nMessage(module, channel, "info.noentries",
                        "commander", senderName,
                        "id", timerStr,
                        "interval", t.getInterval() / 1000);
            } else {
                boolean first = true;
                for(TimerEntry entry : t.getEntries()) {
                    if(!first)
                        sb.append(", ");
                    first = false;
                    sb.append("\"");
                    sb.append(Shorten.genPreview(entry.getMessage(), 30));
                    sb.append("\"");
                }
                Chat.i18nMessage(module, channel, "info.success",
                        "commander", senderName,
                        "id", timerStr,
                        "interval", t.getInterval() / 1000,
                        "entries", sb.toString()
                );
            }
        }
    }

    @Override
    public String getUsage() {
        return "info <timer>";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }
}
