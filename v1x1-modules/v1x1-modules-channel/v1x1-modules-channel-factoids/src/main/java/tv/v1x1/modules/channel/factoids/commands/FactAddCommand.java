package tv.v1x1.modules.channel.factoids.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.common.util.text.Shorten;
import tv.v1x1.modules.channel.factoids.FactoidsModule;
import tv.v1x1.modules.channel.factoids.dao.Factoid;

import java.util.List;

/**
 * @author Josh
 */
public class FactAddCommand extends Command {
    final private FactoidsModule module;

    public FactAddCommand(FactoidsModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("add");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getDisplayName();
        final String factName = args.remove(0);
        final String factData = String.join(" ", args);
        final Factoid oldFact = module.getFact(channel.getTenant(), factName);
        if(oldFact != null) {
            Chat.i18nMessage(module, channel, "alreadyexists",
                    "commander", commander,
                    "fact", oldFact.getId());
            return;
        }
        final Factoid fact = module.addFact(channel.getTenant(), factName, factData, null);
        if(fact == null) {
            Chat.i18nMessage(module, channel, "generic.error",
                    "commander", commander,
                    "message", "addFact() returned null");
        } else {
            Chat.i18nMessage(module, channel, "add.success",
                    "commander", commander,
                    "id", fact.getId(),
                    "fact", Shorten.genPreview(fact.getData()));
        }
    }

    @Override
    public String getUsage() {
        return "<fact> <response>";
    }

    @Override
    public String getDescription() {
        return "add a fact";
    }

    @Override
    public String getHelp() {
        return "facts are called upon as a custom command";
    }

    @Override
    public int getMinArgs() {
        return 2;
    }

    @Override
    public void handleArgMismatch(final ChatMessage chatMessage, final String command, final List<String> args) {
        if(args.size() < 2) {
            Chat.i18nMessage(module, chatMessage.getChannel(), "invalid.args",
                    "commander", chatMessage.getSender().getDisplayName(),
                    "usage", getUsage());
        }
    }
}
