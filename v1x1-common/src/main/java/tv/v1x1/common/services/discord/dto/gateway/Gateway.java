package tv.v1x1.common.services.discord.dto.gateway;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gateway {
    @JsonProperty
    private String url;

    public Gateway() {
    }

    public Gateway(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
