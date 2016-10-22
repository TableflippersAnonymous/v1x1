package tv.v1x1.modules.channel.echo;

import tv.v1x1.common.dto.messages.events.ChatJoinEvent;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.ChatPartEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;

/**
 * @author Josh
 */
public class EchoListener implements EventListener {

    Echo echo;

    EchoListener(final Echo echo) {
        this.echo = echo;
    }

    @EventHandler
    public void onChatJoin(final ChatJoinEvent ev) {
        if(!echo.getTenantConfiguration(ev.getChannel().getTenant()).isEnabled())
            return;
        final StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getUser().getDisplayName());
        sb.append(" joined the channel");
        echo.crsc.sendMessage(ev.getChannel(), sb.toString());
    }

    @EventHandler
    public void onChatPart(final ChatPartEvent ev) {
        if(!echo.getTenantConfiguration(ev.getChannel().getTenant()).isEnabled())
            return;
        final StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getUser().getDisplayName());
        sb.append(" left the channel");
        echo.crsc.sendMessage(ev.getChannel(), sb.toString());
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(!echo.getTenantConfiguration(ev.getChatMessage().getChannel().getTenant()).isEnabled())
            return;
        System.out.println("Got chat message event");
        final StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getChatMessage().getText());
        echo.crsc.sendMessage(ev.getChatMessage().getChannel(), sb.toString());
    }
}
