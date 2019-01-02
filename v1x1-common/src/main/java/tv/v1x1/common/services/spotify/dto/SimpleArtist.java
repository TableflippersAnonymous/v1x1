package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleArtist extends Context {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;

    public SimpleArtist() {
    }

    public SimpleArtist(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                        final String id, final String name) {
        super(type, href, externalUrls, uri);
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
