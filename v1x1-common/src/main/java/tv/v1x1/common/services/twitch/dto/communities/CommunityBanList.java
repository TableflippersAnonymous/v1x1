package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityBanList {
    @JsonProperty("_cursor")
    private String cursor;
    @JsonProperty("banned_users")
    private List<CommunityBannedUser> bannedUsers;

    public CommunityBanList() {
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(final String cursor) {
        this.cursor = cursor;
    }

    public List<CommunityBannedUser> getBannedUsers() {
        return bannedUsers;
    }

    public void setBannedUsers(final List<CommunityBannedUser> bannedUsers) {
        this.bannedUsers = bannedUsers;
    }
}
