package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityTimedOutUser extends CommunityBannedUser {
    @JsonProperty("end_timestamp")
    private long endTimestamp;

    public CommunityTimedOutUser() {
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(final long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
}
