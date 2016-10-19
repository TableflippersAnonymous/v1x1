package tv.twitchbot.modules.channel.i18n_test;

import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.modules.eventhandler.EventHandler;
import tv.twitchbot.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class I18nTestListener implements EventListener {
    I18nTest module;

    I18nTestListener(I18nTest module) {
        this.module = module;
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        module.delegator.handleChatMessage(ev);
    }
}
