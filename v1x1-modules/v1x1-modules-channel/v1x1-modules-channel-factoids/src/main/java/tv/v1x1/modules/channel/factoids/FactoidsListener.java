package tv.v1x1.modules.channel.factoids;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TenantConfigChangeEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class FactoidsListener implements EventListener {
    final private FactoidsModule module;
    FactoidsListener(final FactoidsModule factoidsModule) {
        this.module = factoidsModule;
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            module.handleMessage(ev);
    }

    @EventHandler
    public void onTenantConfigChange(TenantConfigChangeEvent ev) {
        module.pruneBlanks(ev.getTenant());
        module.pruneAliases(ev.getTenant());
    }
}
