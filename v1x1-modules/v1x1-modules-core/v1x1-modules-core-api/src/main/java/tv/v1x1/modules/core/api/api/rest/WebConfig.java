package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

import java.util.Map;

/**
 * Created by naomi on 7/12/2017.
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
    @JsonProperty("config_oauth_urls")
    private Map<String, String> configOauthUrls;

    public WebConfig() {
    }

    public WebConfig(final Map<Platform, String> clientIds, final Map<Platform, String> redirectUris,
                     final String apiBase, final String pubsubBase, final Map<String, String> configOauthUrls) {
        this.clientIds = clientIds;
        this.redirectUris = redirectUris;
        this.apiBase = apiBase;
        this.pubsubBase = pubsubBase;
        this.configOauthUrls = configOauthUrls;
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

    public Map<String, String> getConfigOauthUrls() {
        return configOauthUrls;
    }

    public void setConfigOauthUrls(final Map<String, String> configOauthUrls) {
        this.configOauthUrls = configOauthUrls;
    }
}
