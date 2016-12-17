package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 12/17/2016.
 */
public class StateResponse {
    @JsonProperty
    private String state;

    @JsonProperty
    private long ttl;

    public StateResponse() {
    }

    public StateResponse(final String state, final long ttl) {
        this.state = state;
        this.ttl = ttl;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(final long ttl) {
        this.ttl = ttl;
    }
}
