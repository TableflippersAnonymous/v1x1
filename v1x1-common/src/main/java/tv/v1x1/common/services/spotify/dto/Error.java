package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    @JsonProperty
    private long status;
    @JsonProperty
    private String message;

    public Error() {
    }

    public Error(final long status, final String message) {
        this.status = status;
        this.message = message;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(final long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
