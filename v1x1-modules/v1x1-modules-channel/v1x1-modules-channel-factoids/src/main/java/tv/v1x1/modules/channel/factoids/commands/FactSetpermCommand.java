package tv.v1x1.modules.channel.factoids.commands;

import com.google.common.collect.ImmutableList;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.factoids.FactoidsModule;
import tv.v1x1.modules.channel.factoids.Factoid;

import java.util.List;

/**
 * @author Josh
 */
public class FactSetpermCommand extends Command {
    final private FactoidsModule module;

    public FactSetpermCommand(FactoidsModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("setperm");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getDisplayName();
        final String factName = args.get(0).toLowerCase();
        final String factPerm = FactoidsModule.CUSTOM_PREM_PREFIX + args.get(1);
        final Factoid oldFact = module.getFact(channel.getTenant(), factName);
        if(oldFact == null) {
            Chat.i18nMessage(module, channel, "noexist",
            "commander", commander,
                    "id", factName);
            return;
        }
        oldFact.setPermission(new tv.v1x1.common.dto.db.Permission(factPerm));
        final Factoid fact = module.addFact(channel.getTenant(), factName, oldFact);
        if(fact == null) {
            Chat.i18nMessage(module, channel, "generic.error",
                    "commander", commander,
                    "message", "addFact() returned null");
        } else {
            Chat.i18nMessage(module, channel, "setperm.success",
                    "commander", commander,
                    "id", factName,
                    "perm", fact.getPermission().getNode());
        }
    }

    @Override
    public String getUsage() {
        return "<fact> <perm>";
    }

    @Override
    public String getDescription() {
        return "set the permission for a fact";
    }

    @Override
    public String getHelp() {
        return "[UNSUPPORTED] permissions are set on v1x1 groups and this is probably confusing and might be changed in the future";
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
