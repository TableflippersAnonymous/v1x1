package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {
    @JsonProperty
    private long height;
    @JsonProperty
    private String url;
    @JsonProperty
    private long width;

    public Image() {
    }

    public Image(final long height, final String url, final long width) {
        this.height = height;
        this.url = url;
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(final long height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(final long width) {
        this.width = width;
    }
}
