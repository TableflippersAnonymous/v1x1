package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 12/18/2016.
 */
public class LongTermTokenRequest {
    @JsonProperty
    private long ttl = 3600000;
    @JsonProperty
    private List<String> permissions;

    public LongTermTokenRequest() {
    }

    public LongTermTokenRequest(final long ttl, final List<String> permissions) {
        this.ttl = ttl;
        this.permissions = permissions;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(final long ttl) {
        this.ttl = ttl;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = permissions;
    }
}
