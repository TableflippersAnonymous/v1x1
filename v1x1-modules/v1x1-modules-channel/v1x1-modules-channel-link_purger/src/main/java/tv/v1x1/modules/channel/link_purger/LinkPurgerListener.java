package tv.v1x1.modules.channel.link_purger;

import com.google.common.collect.ImmutableMap;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.irc.commands.PrivmsgCommand;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author Josh
 */
public class LinkPurgerListener implements EventListener {
    private final LinkPurger module;

    public LinkPurgerListener(LinkPurger linkPurger) {
        module = linkPurger;
    }

    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        module.delegator.handleChatMessage(ev);
        if(!shouldMonitorUser(ev)) return;
        final Channel channel = ev.getChatMessage().getChannel();
        final User sender = ev.getChatMessage().getSender();
        if(module.usePermit(channel, sender)) return;
        final String[] words = ev.getChatMessage().getText().split(" ");
        for(final String word : words) {
            final URL url;
            if(!word.contains(".")) continue;
            try {
                if(!word.matches("^.+://.+")) url = new URL("http://" + word);
                else url = new URL(word);
            } catch (MalformedURLException e) {
                continue;
            }
            String host = url.getHost();
            if(!canResolve(host)) continue;
            punish(channel, sender, module.addOffense(channel, sender));
            break;
        }
    }

    private void punish(final Channel channel, final User sender, final int offenseNumber) {
        if(offenseNumber < 2) {
            Chat.purge(module, channel, sender, 1, "Unpermitted link");
            Chat.message(module, channel, module.language.message(module.toDto(), "purged", ImmutableMap.of("user", sender.getDisplayName())));
        } else {
            Chat.punish(module, channel, sender, 600, "Unpermitted link");
            Chat.message(module, channel, module.language.message(module.toDto(), "timeout", ImmutableMap.of("user", sender.getDisplayName())));
        }
    }

    private boolean shouldMonitorUser(ChatMessageEvent ev) {
        // TODO: Support other platforms
        if(!(ev instanceof TwitchChatMessageEvent)) return false;
        PrivmsgCommand msg = ((TwitchChatMessageEvent) ev).getPrivmsgCommand();
        return !(msg.isMod() || msg.getBadges().contains(PrivmsgCommand.Badge.BROADCASTER) || msg.getUserType() != null);

    }

    private boolean canResolve(final String host) {
        try {
            return !reservedAddress(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private boolean reservedAddress(final InetAddress ip) {
        return ip.isLinkLocalAddress() ||
                ip.isLoopbackAddress() ||
                ip.isMulticastAddress() ||
                ip.isSiteLocalAddress();
    }
}
