package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbedFooter {
    @JsonProperty
    private String text;
    @JsonProperty("icon_url")
    private String iconUrl;
    @JsonProperty("proxy_icon_url")
    private String proxyIconUrl;

    public EmbedFooter() {
    }

    public EmbedFooter(final String text, final String iconUrl, final String proxyIconUrl) {
        this.text = text;
        this.iconUrl = iconUrl;
        this.proxyIconUrl = proxyIconUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
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
