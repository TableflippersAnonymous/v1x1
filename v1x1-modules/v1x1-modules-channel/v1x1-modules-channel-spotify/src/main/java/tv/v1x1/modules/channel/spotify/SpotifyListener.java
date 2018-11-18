package tv.v1x1.modules.channel.spotify;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.util.commands.CommandDelegator;

public class SpotifyListener implements EventListener {
    private final SpotifyModule module;
    private final CommandDelegator delegator;

    public SpotifyListener(final SpotifyModule module, final CommandDelegator delegator) {
        this.module = module;
        this.delegator = delegator;
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(module.isEnabled(ev.getChatMessage().getChannel()))
            delegator.handleChatMessage(ev);
    }
}
