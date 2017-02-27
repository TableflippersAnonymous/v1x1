package tv.v1x1.common.services.twitch.dto.communities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

import java.util.List;

/**
 * Created by cobi on 2/27/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommunityModeratorList {
    @JsonProperty
    private List<User> moderators;

    public CommunityModeratorList() {
    }

    public List<User> getModerators() {
        return moderators;
    }

    public void setModerators(final List<User> moderators) {
        this.moderators = moderators;
    }
}
