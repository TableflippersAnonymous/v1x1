package tv.v1x1.modules.channel.factoids.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.text.Shorten;
import tv.v1x1.modules.channel.factoids.Factoid;
import tv.v1x1.modules.channel.factoids.FactoidsModule;

import java.util.List;

/**
 * @author Josh
 */
public class FactInfoCommand extends Command {
    final private FactoidsModule module;

    public FactInfoCommand(FactoidsModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("info");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getDisplayName();
        final String factName = args.get(0).toLowerCase();
        final Factoid fact = module.getFactDirectly(channel.getChannelGroup().getTenant(), factName);
        if(fact == null) {
            Chat.i18nMessage(module, channel, "noexist",
            "commander", commander,
                    "id", factName);
        } else {
            if(fact.isAlias()) {
                Chat.i18nMessage(module, channel, "info.alias",
                        "commander", commander,
                        "id", factName,
                        "to", fact.getData());
            } else {
                if(fact.getPermission() == null) {
                    Chat.i18nMessage(module, channel, "info.noperm",
                            "commander", commander,
                            "id", factName,
                            "enabled", (fact.isHidden() ? "hidden" : "not hidden"),
                            "fact", Shorten.genPreview(fact.getData(), 100));
                } else {
                    Chat.i18nMessage(module, channel, "info.standard",
                            "commander", commander,
                            "id", factName,
                            "enabled", (fact.isHidden() ? "hidden" : "not hidden"),
                            "perm", fact.getPermission().getNode(),
                            "fact", Shorten.genPreview(fact.getData(), 100));
                }
            }
        }
    }

    @Override
    public String getUsage() {
        return "<fact>";
    }

    @Override
    public String getDescription() {
        return "get information about a fact";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        if(args.size() < 1) {
            Chat.i18nMessage(module, chatMessage.getChannel(), "invalid.args",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "usage", getUsage());
        }
    }
}
