package tv.v1x1.common.services.twitch.dto.teams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.services.twitch.dto.users.User;

import java.util.List;

/**
 * Created by cobi on 2/28/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team extends ShortTeam {
    @JsonProperty
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(final List<User> users) {
        this.users = users;
    }
}
