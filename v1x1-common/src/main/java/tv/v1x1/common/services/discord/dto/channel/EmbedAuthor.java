package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbedAuthor {
    @JsonProperty
    private String name;
    @JsonProperty
    private String url;
    @JsonProperty("icon_url")
    private String iconUrl;
    @JsonProperty("proxy_icon_url")
    private String proxyIconUrl;

    public EmbedAuthor() {
    }

    public EmbedAuthor(final String name, final String url, final String iconUrl, final String proxyIconUrl) {
        this.name = name;
        this.url = url;
        this.iconUrl = iconUrl;
        this.proxyIconUrl = proxyIconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(final String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getProxyIconUrl() {
        return proxyIconUrl;
    }

    public void setProxyIconUrl(final String proxyIconUrl) {
        this.proxyIconUrl = proxyIconUrl;
    }
}
