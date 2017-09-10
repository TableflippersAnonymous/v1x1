package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {
    @JsonProperty
    private String id;
    @JsonProperty
    private String filename;
    @JsonProperty
    private Integer size;
    @JsonProperty
    private String url;
    @JsonProperty("proxy_url")
    private String proxyUrl;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private Integer width;

    public Attachment() {
    }

    public Attachment(final String id, final String filename, final Integer size, final String url,
                      final String proxyUrl, final Integer height, final Integer width) {
        this.id = id;
        this.filename = filename;
        this.size = size;
        this.url = url;
        this.proxyUrl = proxyUrl;
        this.height = height;
        this.width = width;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(final Integer size) {
        this.size = size;
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
