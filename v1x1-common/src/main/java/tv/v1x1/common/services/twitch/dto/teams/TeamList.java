package tv.v1x1.common.services.twitch.dto.teams;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/30/2016.
 */
public class TeamList {
    @JsonProperty
    private List<Team> teams;

    public TeamList() {
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(final List<Team> teams) {
        this.teams = teams;
    }
}
