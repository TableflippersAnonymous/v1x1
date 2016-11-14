package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

import java.util.UUID;

/**
 * Created by naomi on 10/26/2016.
 */
public class User {
    @JsonProperty("global_user_id")
    private UUID globalUserId;
    @JsonProperty
    private Platform platform;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("display_name")
    private String displayName;

    public User() {
    }

    public User(final UUID globalUserId, final Platform platform, final String userId, final String displayName) {
        this.globalUserId = globalUserId;
        this.platform = platform;
        this.userId = userId;
        this.displayName = displayName;
    }

    public UUID getGlobalUserId() {
        return globalUserId;
    }

    public void setGlobalUserId(final UUID globalUserId) {
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
}
