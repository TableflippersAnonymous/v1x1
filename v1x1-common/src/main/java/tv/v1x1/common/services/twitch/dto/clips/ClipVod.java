package tv.v1x1.common.services.twitch.dto.clips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClipVod {
    @JsonProperty
    private String id;
    @JsonProperty
    private String url;

    public ClipVod() {
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
