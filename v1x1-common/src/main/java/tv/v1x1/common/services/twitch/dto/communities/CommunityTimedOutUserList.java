package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityTimedOutUserList {
    @JsonProperty("_cursor")
    private String cursor;
    @JsonProperty("timed_out_users")
    private List<CommunityTimedOutUser> timedOutUsers;

    public CommunityTimedOutUserList() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public List<CommunityTimedOutUser> getTimedOutUsers() {
        return timedOutUsers;
    }

    public void setTimedOutUsers(final List<CommunityTimedOutUser> timedOutUsers) {
        this.timedOutUsers = timedOutUsers;
    }
}
