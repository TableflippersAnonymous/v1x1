package tv.v1x1.common.services.twitch.dto.teams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamList {
    @JsonProperty
    private List<ShortTeam> teams;

    public TeamList() {
    }

    public List<ShortTeam> getTeams() {
        return teams;
    }

    public void setTeams(final List<ShortTeam> teams) {
        this.teams = teams;
    }
}
