package tv.v1x1.modules.channel.factoids.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.factoids.FactoidsModule;

import java.util.List;

/**
 * @author Josh
 */
public class FactEnableCommand extends Command {
    final private FactoidsModule module;

    public FactEnableCommand(FactoidsModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("unhide", "enable");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getMention();
        final String factName = args.remove(0).toLowerCase();
        final boolean result = module.hideFact(channel.getChannelGroup().getTenant(), factName, false);
        if(!result) {
            Chat.i18nMessage(module, channel, "noexist",
                    "commander", commander,
                    "id", factName);
            return;
        }
        Chat.i18nMessage(module, channel, "toggle.success",
                "commander", commander,
                "id", factName,
                "status", "unhidden");
    }

    @Override
    public String getUsage() {
        return "<fact>";
    }

    @Override
    public String getDescription() {
        return "unhide a fact";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        if(args.size() < 1) {
            Chat.i18nMessage(module, chatMessage.getChannel(), "invalid.args",
                    "commander", chatMessage.getSender().getMention(),
                    "usage", getUsage());
        }
    }
}
