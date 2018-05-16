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

/**
 * @author Josh
 */
/* pkg-private */ class TimerInfoCommand extends Command {
    private final TimedMessages module;

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
        final String commander = chatMessage.getSender().getMention();
        final String timerStr = args.get(0);
        final Timer t = module.getTimer(channel.getChannelGroup().getTenant(), timerStr);
        if(t == null) {
            Chat.i18nMessage(module, channel, "invalid.timer",
                    "commander", commander,
            "id", timerStr);
        } else {
            final StringBuilder sb = new StringBuilder();
            if(t.getEntries().size() < 1) {
                Chat.i18nMessage(module, channel, "info.noentries",
                        "commander", commander,
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
                        "commander", commander,
                        "id", timerStr,
                        "interval", t.getInterval() / 1000,
                        "enabled", (t.isEnabled() ? "enabled" : "disabled"),
                        "entries", sb.toString()
                );
            }
        }
    }

    @Override
    public String getUsage() {
        return "<timer>";
    }

    @Override
    public String getDescription() {
        return "get info on a rotation";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getMention();
        Chat.i18nMessage(module, channel, "info.notarget",
                "commander", commander,
                "usage", getUsage());
    }
}
