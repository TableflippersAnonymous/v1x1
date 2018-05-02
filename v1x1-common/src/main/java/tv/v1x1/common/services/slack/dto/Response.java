package tv.v1x1.common.services.slack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    @JsonProperty
    private boolean ok = false;
    @JsonProperty
    private String error;

    public Response() {
    }

    public Response(final boolean ok) {
        this.ok = ok;
    }

    public Response(final String error) {
        this(false);
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(final boolean ok) {
        this.ok = ok;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }
}
