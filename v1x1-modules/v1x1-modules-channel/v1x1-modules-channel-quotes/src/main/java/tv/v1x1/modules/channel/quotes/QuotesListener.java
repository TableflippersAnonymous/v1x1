package tv.v1x1.modules.channel.quotes;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class QuotesListener implements EventListener {
    private final QuotesModule module;

    QuotesListener(final QuotesModule quotes) {
        module = quotes;
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            module.delegator.handleChatMessage(ev);
    }
}
