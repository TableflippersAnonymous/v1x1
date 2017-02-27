package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmoticonImageEntry {
    @JsonProperty
    private int width;
    @JsonProperty
    private int height;
    @JsonProperty
    private String url;
    @JsonProperty("emoticon_set")
    private long emoticonSet;

    public EmoticonImageEntry() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public long getEmoticonSet() {
        return emoticonSet;
    }

    public void setEmoticonSet(final long emoticonSet) {
        this.emoticonSet = emoticonSet;
    }
}
