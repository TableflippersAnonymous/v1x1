package tv.v1x1.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 4/15/2017.
 */
public class ZipkinConfig {
    private boolean enabled;
    private String uri;

    public ZipkinConfig() {
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(final String uri) {
        this.uri = uri;
    }

    @JsonProperty("enabled")
    public boolean isEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}
