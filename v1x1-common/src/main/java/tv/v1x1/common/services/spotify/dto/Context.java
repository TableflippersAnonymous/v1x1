package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Context {
    @JsonProperty
    private String type;
    @JsonProperty
    private String href;
    @JsonProperty("external_urls")
    private Map<String, String> externalUrls;
    @JsonProperty
    private String uri;

    public Context() {
    }

    public Context(final String type, final String href, final Map<String, String> externalUrls, final String uri) {
        this.type = type;
        this.href = href;
        this.externalUrls = externalUrls;
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
    }

    public Map<String, String> getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(final Map<String, String> externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }
}
