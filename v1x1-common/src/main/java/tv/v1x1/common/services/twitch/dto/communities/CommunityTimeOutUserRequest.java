package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityTimeOutUserRequest {
    @JsonProperty
    private int duration;
    @JsonProperty
    private String reason;

    public CommunityTimeOutUserRequest() {
    }

    public CommunityTimeOutUserRequest(final int duration) {
        this.duration = duration;
    }

    public CommunityTimeOutUserRequest(final int duration, final String reason) {
        this.duration = duration;
        this.reason = reason;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }
}
