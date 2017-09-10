package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbedThumbnail {
    @JsonProperty
    private String url;
    @JsonProperty("proxy_url")
    private String proxyUrl;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private Integer width;

    public EmbedThumbnail() {
    }

    public EmbedThumbnail(final String url, final String proxyUrl, final Integer height, final Integer width) {
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.height = height;
        this.width = width;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(final String proxyUrl) {
        this.proxyUrl = proxyUrl;
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
