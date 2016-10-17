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

    EchoListener(Echo echo) {
        this.echo = echo;
    }

    @EventHandler
    public void onChatJoin(ChatJoinEvent ev) {
        StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getUser().getDisplayName());
        sb.append(" joined the channel");
        echo.crsc.sendMessage(ev.getChannel(), sb.toString());
    }

    @EventHandler
    public void onChatPart(ChatPartEvent ev) {
        StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getUser().getDisplayName());
        sb.append(" left the channel");
        echo.crsc.sendMessage(ev.getChannel(), sb.toString());
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        StringBuilder sb = new StringBuilder();
        sb.append("Echo: ");
        sb.append(ev.getChatMessage().getText());
        echo.crsc.sendMessage(ev.getChatMessage().getChannel(), ev.getChatMessage().getText());
    }
}
