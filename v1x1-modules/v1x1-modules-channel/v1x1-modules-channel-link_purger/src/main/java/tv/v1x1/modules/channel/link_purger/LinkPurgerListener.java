package tv.v1x1.modules.channel.link_purger;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.irc.MessageTaggedIrcStanza;
import tv.v1x1.common.dto.irc.commands.PrivmsgCommand;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.dto.messages.events.TwitchChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * @author Josh
 */
public class LinkPurgerListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Permission whitelistPerm = new Permission("link_purger.permit");
    private final LinkPurger module;

    public LinkPurgerListener(LinkPurger linkPurger) {
        module = linkPurger;
    }

    /*
    Eventually, fix these cases:
    http://2809293708/          - octal IP -> decimal
    http://0xA7726B8C           - octal IP -> hex
    http://[::ffff:A772:6B8C]   - IPv6/No dot
    hxxp://google.com           - invalid protocol
     */
    @EventHandler
    public void onChatMessage(final ChatMessageEvent ev) {
        if(!module.getTenantConfiguration(ev.getChatMessage().getChannel().getTenant()).isEnabled())
            return;
        module.delegator.handleChatMessage(ev);
        if(!shouldMonitorUser(ev)) return;
        final Channel channel = ev.getChatMessage().getChannel();
        final User sender = ev.getChatMessage().getSender();
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
            if(module.usePermit(channel, sender)) return;
            punish(channel, sender, module.addOffense(channel, sender));
            break;
        }
    }

    private void punish(final Channel channel, final User sender, final int offenseNumber) {
        if(offenseNumber < 2) {
            Chat.purge(module, channel, sender, 1, "Unpermitted link");
            Chat.i18nMessage(module, channel, "purged",
                    "user", sender.getDisplayName()
            );
        } else {
            Chat.punish(module, channel, sender, 600, "Unpermitted link");
            Chat.i18nMessage(module, channel, "timeout",
                    "user", sender.getDisplayName()
            );
        }
    }

    private boolean shouldMonitorUser(ChatMessageEvent ev) {
        return !ev.getChatMessage().getPermissions().contains(whitelistPerm);
    }

    private boolean canResolve(final String host) {
        try {
            return !reservedAddress(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private boolean reservedAddress(final InetAddress ip) {
        LOG.debug("Checking if {} is a reserved address", ip.getHostAddress());
        return ip.isLinkLocalAddress() ||
                ip.isLoopbackAddress() ||
                ip.isMulticastAddress() ||
                ip.isAnyLocalAddress() ||
                ip.isSiteLocalAddress();
    }
}
