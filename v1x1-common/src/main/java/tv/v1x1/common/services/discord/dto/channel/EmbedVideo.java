package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbedVideo {
    @JsonProperty
    private String url;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private Integer width;

    public EmbedVideo() {
    }

    public EmbedVideo(final String url, final Integer height, final Integer width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(final Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(final Integer width) {
        this.width = width;
    }
}
