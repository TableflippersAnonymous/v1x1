package tv.v1x1.modules.core.api.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.dto.db.Platform;

import java.util.UUID;

/**
 * @author Josh
 */
public class Channel {
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("platform")
    private String platform;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("channel_id")
    private String channelId;

    public Channel() {
        // For Jackson
    }

    public Channel(final UUID tenantId, final Platform platform, final String displayName, final String channelId) {
        this.tenantId = tenantId.toString();
        this.platform = platform.name();
        this.displayName = displayName;
        this.channelId = channelId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(final String platform) {
        this.platform = platform;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(final String channelId) {
        this.channelId = channelId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
}
