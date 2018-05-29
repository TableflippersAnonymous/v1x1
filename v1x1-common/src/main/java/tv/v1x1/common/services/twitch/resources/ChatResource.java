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
    private final WebTarget chat;

    public ChatResource(final WebTarget chat) {
        this.chat = chat;
    }

    /**
     * Gets a list of badges that can be used in chat for a specified channel.
     * @param channelId ID of channel
     */
    public BadgeList getChannelBadges(final String channelId) {
        return chat.path(channelId).path("badges").request(TwitchApi.ACCEPT).get(BadgeList.class);
    }

    /**
     * Gets all chat emoticons (not including their images).
     * @apiNote This endpoint returns a large amount of data.
     */
    public EmoticonImageList getEmoticonImages() {
        return chat.path("emoticon_images").request(TwitchApi.ACCEPT).get(EmoticonImageList.class);
    }

    /**
     * Gets all chat emoticons (not including their images) in one or more specified sets.
     * @param emoticonSets Specifies the set(s) of emoticons to retrieve.
     */
    public EmoticonSetList getEmoticonSets(final List<String> emoticonSets) {
        return chat.path("emoticon_images").queryParam("emotesets", Joiner.on(",").join(emoticonSets)).request(TwitchApi.ACCEPT).get(EmoticonSetList.class);
    }

    /**
     * Gets all chat emoticons (including their images).
     * @apiNote This endpoint returns a large amount of data.
     */
    public EmoticonList getEmoticons() {
        return chat.path("emoticons").request(TwitchApi.ACCEPT).get(EmoticonList.class);
    }
}
