package tv.v1x1.modules.channel.factoids.commands;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.Command;
import tv.v1x1.modules.channel.factoids.Factoid;
import tv.v1x1.modules.channel.factoids.FactoidsModule;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Josh
 */
public class FactListCommand extends Command {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    final private FactoidsModule module;

    public FactListCommand(FactoidsModule module) {
        this.module = module;
    }

    @Override
    public List<String> getCommands() {
        return ImmutableList.of("list");
    }

    @Override
    public void run(final ChatMessage chatMessage, final String command, final List<String> args) {
        final Channel channel = chatMessage.getChannel();
        final String commander = chatMessage.getSender().getDisplayName();
        final Set<Map.Entry<String, Factoid>> facts = module.getFacts(channel.getChannelGroup().getTenant());
        if(facts == null || facts.isEmpty()) {
            Chat.i18nMessage(module, channel, "list.nofacts",
            "commander", commander);
        } else {
            final HashMap<String, List<String>> aliasMap = new HashMap<>();
            for(Map.Entry<String, Factoid> fact : facts) {
                if(fact.getValue().isAlias())
                    handleAlias(fact, aliasMap);
                else
                    handleFact(fact, aliasMap);
            }
            Chat.i18nMessage(module, channel, "list",
                    "commander", commander,
                    "list", formatAliasMap(aliasMap));
        }
    }

    private void handleAlias(final Map.Entry<String, Factoid> factoid, final HashMap<String, List<String>> aliasMap) {
        LOG.debug("handleAlias() {} -> {}", factoid.getKey(), factoid.getValue().getData());
        final String aliasName = factoid.getKey();
        final String aliasTarget = factoid.getValue().getData();
        aliasMap.putIfAbsent(aliasTarget, new ArrayList<String>());
        final List<String> factEntry = aliasMap.get(aliasTarget);
        factEntry.add(aliasName);
    }

    private void handleFact(final Map.Entry<String, Factoid> factoid, final HashMap<String, List<String>> aliasMap) {
        aliasMap.putIfAbsent(factoid.getKey(), new ArrayList<String>());
    }

    private String formatAliasMap(final HashMap<String, List<String>> aliasMap) {
        final StringBuilder sb = new StringBuilder();
        boolean firstFact = true;
        for(java.util.Map.Entry entry : aliasMap.entrySet()) {
            final List<String> aliases = ((List<String>)entry.getValue());
            if(!firstFact) sb.append(", ");
            firstFact = false;
            sb.append(entry.getKey());
            if(aliases.isEmpty())
                continue;
            sb.append(" (AKA: ");
            boolean firstAlias = true;
            for(String alias : aliases) {
                if(!firstAlias) sb.append(", ");
                firstAlias = false;
                sb.append(alias);
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getDescription() {
        return "list all facts";
    }

    @Override
    public int getMinArgs() {
        return 0;
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
