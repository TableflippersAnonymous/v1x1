package tv.v1x1.common.services.twitch.dto.emotes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 10/29/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmoticonImage {
    @JsonProperty("emoticon_set")
    private Integer emoticonSet;
    @JsonProperty
    private int height;
    @JsonProperty
    private int width;
    @JsonProperty
    private String url;

    public EmoticonImage() {
    }

    public Integer getEmoticonSet() {
        return emoticonSet;
    }

    public void setEmoticonSet(final Integer emoticonSet) {
        this.emoticonSet = emoticonSet;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
