package tv.v1x1.modules.channel.link_purger;

import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.modules.eventhandler.EventHandler;
import tv.v1x1.common.modules.eventhandler.EventListener;
import tv.v1x1.common.services.chat.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Josh
 */
public class LinkPurgerListener implements EventListener {
    private final LinkPurger module;
    private final Pattern urlRegex = Pattern.compile("(?:https?:\\/\\/)?(?:[-a-zA-Z0-9@:%_\\+~#=]+\\.)+[a-z]{2,6}\\b(?:[-a-zA-Z0-9@:%_\\+.~#?&\\/\\/=]*)", Pattern.CASE_INSENSITIVE);

    public LinkPurgerListener(LinkPurger linkPurger) {
        module = linkPurger;
    }

    @EventHandler
    public void onChatMessage(ChatMessageEvent ev) {
        List<String> links = findLinks(ev.getChatMessage().getText());
        Chat.message(module, ev.getChatMessage().getChannel(), "Link Purger: Detected " + links.size() + " links");
        if(links.size() > 0) {
            Chat.purge(module, ev.getChatMessage().getChannel(), ev.getChatMessage().getSender(), "Unpermitted link");
        }
    }

    private List<String> findLinks(String line) {
        final ArrayList<String> links = new ArrayList<>();
        Matcher m = urlRegex.matcher(line);
        while(m.find()) {
            links.add(m.group());
        }
        return links;
    }
}
