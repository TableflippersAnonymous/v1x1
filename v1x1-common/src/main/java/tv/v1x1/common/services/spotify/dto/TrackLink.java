package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackLink extends Context {
    @JsonProperty
    private String id;

    public TrackLink() {
    }

    public TrackLink(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                     final String id) {
        super(type, href, externalUrls, uri);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }
}
