package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/29/2016.
 */
public class EmoticonImageList {
    @JsonProperty
    private List<EmoticonImage> emoticons;

    public EmoticonImageList() {
    }

    public List<EmoticonImage> getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(final List<EmoticonImage> emoticons) {
        this.emoticons = emoticons;
    }
}
