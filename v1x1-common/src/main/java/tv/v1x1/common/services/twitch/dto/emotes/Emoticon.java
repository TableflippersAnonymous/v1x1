package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Emoticon {
    @JsonProperty
    private String regex;
    @JsonProperty
    private List<EmoticonImageEntry> images;

    public Emoticon() {
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(final String regex) {
        this.regex = regex;
    }

    public List<EmoticonImageEntry> getImages() {
        return images;
    }

    public void setImages(final List<EmoticonImageEntry> images) {
        this.images = images;
    }
}
