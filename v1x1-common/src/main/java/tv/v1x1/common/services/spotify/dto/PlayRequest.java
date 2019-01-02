package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayRequest {
    @JsonProperty
    private List<String> uris;
    @JsonProperty("position_ms")
    private Integer positionMs;

    public PlayRequest() {
    }

    public PlayRequest(final List<String> uris, final Integer positionMs) {
        this.uris = uris;
        this.positionMs = positionMs;
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(final List<String> uris) {
        this.uris = uris;
    }

    public Integer getPositionMs() {
        return positionMs;
    }

    public void setPositionMs(final Integer positionMs) {
        this.positionMs = positionMs;
    }
}
