package tv.v1x1.modules.channel.counter;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChatMessage;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.messages.events.ChannelConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChannelGroupConfigChangeEvent;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.counter.commands.CounterCommand;
import tv.v1x1.modules.channel.counter.config.Counter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Josh on 2019-06-25
 */
public class CounterListener implements EventListener {
    final private CounterModule module;
    private CounterLanguage language;
    private CommandDelegator delegator;

    public CounterListener(final CounterModule module) {
        this.module = module;
        this.language = new CounterLanguage();
        delegator = new CommandDelegator("!", module);
        delegator.registerCommand(new CounterCommand(module));
    }

    @EventHandler
    public void onTenantConfigChange(final TenantConfigChangeEvent ev) {
        module.fixConfig(ev.getTenant());
    }

    @EventHandler
    public void onChannelGroupConfigChange(final ChannelGroupConfigChangeEvent ev) {
        module.fixConfig(ev.getChannelGroup());
    }

    @EventHandler
    public void onChannelConfigChange(final ChannelConfigChangeEvent ev) {
        module.fixConfig(ev.getChannel());
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(!module.getConfiguration(ev.getChatMessage().getChannel()).isEnabled()) return;
        if(module.getConfiguration(ev.getChatMessage().getChannel()).isGeekMode())
            tryToIncrement(ev);
        delegator.handleChatMessage(ev);
    }

    private void tryToIncrement(final ChatMessageEvent ev) {
        String identifier = ev.getChatMessage().getText().split(" ", 1)[0];
        if(identifier.endsWith("++") || identifier.endsWith("--")) {
            final String actionStr = identifier.substring(identifier.length()-1);
            final boolean action = actionStr.equals("+");
            identifier = identifier.substring(0, identifier.length()-2).toLowerCase();
            stepCounter(ev.getChatMessage().getChannel(), identifier, action, ev.getChatMessage());
        }
    }

    private boolean hasPerms(final List<Permission> perms, final String identifier,
                             final boolean direction) {
        final String permStr = (direction ? "inc" : "dec");
        return perms.contains(new Permission("counter." + permStr + "all")) ||
                perms.contains(new Permission("counter." + permStr + "." + identifier));
    }

    private void stepCounter(final Channel channel, final String id, final boolean direction, final ChatMessage msg) {
        final Counter counter = module.getCounter(channel, id);
        if(!hasPerms(msg.getPermissions(), id, direction)) return;
        if(counter == null) return;
        String message;
        if(direction) {
            counter.inc();
            message = counter.getIncMessage();
        } else {
            counter.dec();
            message = counter.getDecMessage();
        }
        if(message.isEmpty()) message = CounterLanguage.DEFAULT_SET_MESSAGE;
        module.addCounter(channel, id, counter);
        final Map<String, Object> languageKeys = new HashMap<>(2);
        languageKeys.put("target", id);
        languageKeys.put("count", counter.getCount());
        Chat.message(module, channel, language.format(message, languageKeys));
    }
}
