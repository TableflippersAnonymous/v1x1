package tv.v1x1.modules.channel.ops_tool;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class OpsToolListener implements EventListener {
    private OpsTool opsTool;
    public OpsToolListener(final OpsTool opsTool) {
        this.opsTool = opsTool;
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        if(opsTool.hasGlobalPerm(ev.getChatMessage().getSender()))
            opsTool.delegator.handleChatMessage(ev);
    }
}
