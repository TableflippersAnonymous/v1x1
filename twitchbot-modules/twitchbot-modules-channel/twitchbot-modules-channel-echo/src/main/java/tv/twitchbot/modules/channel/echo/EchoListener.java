package tv.twitchbot.modules.channel.echo;

import tv.twitchbot.common.dto.messages.events.ChatJoinEvent;
import tv.twitchbot.common.dto.messages.events.ChatMessageEvent;
import tv.twitchbot.common.dto.messages.events.ChatPartEvent;
import tv.twitchbot.common.modules.eventhandler.EventHandler;
import tv.twitchbot.common.modules.eventhandler.EventListener;

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
        final StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getUser().getDisplayName());
        sb.append(" joined the channel");
        echo.crsc.sendMessage(ev.getChannel(), sb.toString());
    }

    @EventHandler
    public void onChatPart(final ChatPartEvent ev) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getUser().getDisplayName());
        sb.append(" left the channel");
        echo.crsc.sendMessage(ev.getChannel(), sb.toString());
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        System.out.println("Got chat message event");
        final StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getChatMessage().getText());
        echo.crsc.sendMessage(ev.getChatMessage().getChannel(), sb.toString());
    }
}
