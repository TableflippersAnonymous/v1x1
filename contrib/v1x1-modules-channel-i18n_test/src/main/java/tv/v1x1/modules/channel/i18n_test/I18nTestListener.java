package tv.v1x1.modules.channel.i18n_test;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class I18nTestListener implements EventListener {
    I18nTest module;

    I18nTestListener(final I18nTest module) {
        this.module = module;
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        module.delegator.handleChatMessage(ev);
    }
}
