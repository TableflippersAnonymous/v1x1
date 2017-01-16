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
public class FactAliasCommand extends Command {
    final private FactoidsModule module;

    public FactAliasCommand(FactoidsModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("alias");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getDisplayName();
        final String aliasName = args.get(1);
        final String aliasTargetName = args.get(0);
        final Factoid aliasTarget = module.getFact(channel.getTenant(), aliasTargetName);
        if(aliasTarget == null) {
            Chat.i18nMessage(module, channel, "noexist",
                    "commander", commander,
                    "id", aliasTargetName);
            return;
        }
        final Factoid oldFact = module.getFactDirectly(channel.getTenant(), aliasName);
        if(oldFact != null) {
            Chat.i18nMessage(module, channel, "alreadyexists",
                    "commander", commander,
                    "fact", oldFact.getId());
            return;
        }
        final Factoid fact = module.addFact(channel.getTenant(), aliasName, aliasTarget.getId(), null, true);
        if(fact == null) {
            Chat.i18nMessage(module, channel, "generic.error",
                    "commander", commander,
                    "message", "addFact() returned null");
        } else {
            Chat.i18nMessage(module, channel, "alias.success",
                    "commander", commander,
                    "id", fact.getId(),
                    "alias", fact.getData());
        }
    }

    @Override
    public String getUsage() {
        return "<fact> <alias>";
    }

    @Override
    public String getDescription() {
        return "alias a fact";
    }

    @Override
    public String getHelp() {
        return "aliases can be removed with !fact remove <alias>";
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
