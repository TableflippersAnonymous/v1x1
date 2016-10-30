package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/29/2016.
 */
public class EmoticonList {
    @JsonProperty
    private List<Emoticon> emoticons;

    public EmoticonList() {
    }

    public List<Emoticon> getEmoticons() {
        return emoticons;
    }

    public void setEmoticons(final List<Emoticon> emoticons) {
        this.emoticons = emoticons;
    }
}
