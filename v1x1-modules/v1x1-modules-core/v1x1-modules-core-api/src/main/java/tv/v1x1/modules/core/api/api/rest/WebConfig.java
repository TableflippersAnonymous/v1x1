package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

import java.util.Map;

/**
 * Created by cobi on 7/12/2017.
 */
public class WebConfig {
    @JsonProperty("client_ids")
    private Map<Platform, String> clientIds;
    @JsonProperty("redirect_uris")
    private Map<Platform, String> redirectUris;
    @JsonProperty("api_base")
    private String apiBase;
    @JsonProperty("pubsub_base")
    private String pubsubBase;

    public WebConfig() {
    }

    public WebConfig(final Map<Platform, String> clientIds, final Map<Platform, String> redirectUris, final String apiBase, final String pubsubBase) {
        this.clientIds = clientIds;
        this.redirectUris = redirectUris;
        this.apiBase = apiBase;
        this.pubsubBase = pubsubBase;
    }

    public Map<Platform, String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(final Map<Platform, String> clientIds) {
        this.clientIds = clientIds;
    }

    public Map<Platform, String> getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(final Map<Platform, String> redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getApiBase() {
        return apiBase;
    }

    public void setApiBase(final String apiBase) {
        this.apiBase = apiBase;
    }

    public String getPubsubBase() {
        return pubsubBase;
    }

    public void setPubsubBase(final String pubsubBase) {
        this.pubsubBase = pubsubBase;
    }
}
