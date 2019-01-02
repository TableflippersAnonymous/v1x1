package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerError extends Error {
    @JsonProperty
    private String reason;

    public PlayerError() {
    }

    public PlayerError(final long status, final String message, final String reason) {
        super(status, message);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }
}
