package tv.v1x1.common.services.twitch.unsupported.tmi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 11/13/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {
    @JsonProperty("host_id")
    private long hostId;
    @JsonProperty("target_id")
    private long targetId;
    @JsonProperty("host_login")
    private String hostLogin;
    @JsonProperty("target_login")
    private String targetLogin;
    @JsonProperty("host_display_name")
    private String hostDisplayName;
    @JsonProperty("target_display_name")
    private String targetDisplayName;
    @JsonProperty("host_recent_chat_activity_count")
    private long hostRecentChatActivityCount = 0;
    @JsonProperty("host_partnered")
    private boolean hostPartnered = false;

    public Host() {
    }

    public long getHostId() {
        return hostId;
    }

    public void setHostId(final long hostId) {
        this.hostId = hostId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(final long targetId) {
        this.targetId = targetId;
    }

    public String getHostLogin() {
        return hostLogin;
    }

    public void setHostLogin(final String hostLogin) {
        this.hostLogin = hostLogin;
    }

    public String getTargetLogin() {
        return targetLogin;
    }

    public void setTargetLogin(final String targetLogin) {
        this.targetLogin = targetLogin;
    }

    public String getHostDisplayName() {
        return hostDisplayName;
    }

    public void setHostDisplayName(final String hostDisplayName) {
        this.hostDisplayName = hostDisplayName;
    }

    public String getTargetDisplayName() {
        return targetDisplayName;
    }

    public void setTargetDisplayName(final String targetDisplayName) {
        this.targetDisplayName = targetDisplayName;
    }

    public long getHostRecentChatActivityCount() {
        return hostRecentChatActivityCount;
    }

    public void setHostRecentChatActivityCount(final long hostRecentChatActivityCount) {
        this.hostRecentChatActivityCount = hostRecentChatActivityCount;
    }

    public boolean isHostPartnered() {
        return hostPartnered;
    }

    public void setHostPartnered(final boolean hostPartnered) {
        this.hostPartnered = hostPartnered;
    }
}
