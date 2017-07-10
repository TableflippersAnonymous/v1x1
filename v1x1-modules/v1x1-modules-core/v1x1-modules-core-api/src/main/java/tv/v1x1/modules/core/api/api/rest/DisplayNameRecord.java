package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

import java.util.UUID;

/**
 * Created by naomi on 3/6/2017.
 */
public class DisplayNameRecord {
    @JsonProperty
    private Platform platform;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty
    private String username;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("global_user_id")
    private UUID globalUserId;

    public DisplayNameRecord() {
    }

    public DisplayNameRecord(final Platform platform, final String userId, final String username, final String displayName, final UUID globalUserId) {
        this.platform = platform;
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.globalUserId = globalUserId;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(final Platform platform) {
        this.platform = platform;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public UUID getGlobalUserId() {
        return globalUserId;
    }

    public void setGlobalUserId(final UUID globalUserId) {
        this.globalUserId = globalUserId;
    }
}
