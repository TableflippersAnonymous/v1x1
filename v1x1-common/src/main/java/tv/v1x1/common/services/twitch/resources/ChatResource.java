package tv.v1x1.common.services.twitch.resources;

import com.google.common.base.Joiner;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.channels.BadgeList;
import tv.v1x1.common.services.twitch.dto.emotes.EmoticonImageList;
import tv.v1x1.common.services.twitch.dto.emotes.EmoticonList;
import tv.v1x1.common.services.twitch.dto.emotes.EmoticonSetList;

import javax.ws.rs.client.WebTarget;
import java.util.List;

/**
 * Created by cobi on 10/29/2016.
 */
public class ChatResource {
    private WebTarget chat;

    public ChatResource(final WebTarget chat) {
        this.chat = chat;
    }

    public EmoticonList getEmoticons() {
        return chat.path("emoticons").request(TwitchApi.ACCEPT).get().readEntity(EmoticonList.class);
    }

    public EmoticonImageList getEmoticonImages() {
        return chat.path("emoticon_images").request(TwitchApi.ACCEPT).get().readEntity(EmoticonImageList.class);
    }

    public EmoticonSetList getEmoticonSets(final List<String> emoticonSets) {
        return chat.path("emoticon_images").queryParam("emotesets", Joiner.on(",").join(emoticonSets)).request(TwitchApi.ACCEPT).get().readEntity(EmoticonSetList.class);
    }

    public BadgeList getChannelBadges(final String channel) {
        return chat.path(channel).path("badges").request(TwitchApi.ACCEPT).get().readEntity(BadgeList.class);
    }
}
