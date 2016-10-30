package tv.v1x1.common.services.twitch.resources;

import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.teams.Team;
import tv.v1x1.common.services.twitch.dto.teams.TeamList;

import javax.ws.rs.client.WebTarget;

/**
 * Created by naomi on 10/30/2016.
 */
public class TeamsResource {
    private final WebTarget teams;

    public TeamsResource(final WebTarget teams) {
        this.teams = teams;
    }

    public TeamList getTeams(final Integer limit, final Integer offset) {
        return teams
                .queryParam("limit", limit).queryParam("offset", offset)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(TeamList.class);
    }

    public Team getTeam(final String team) {
        return teams.path(team)
                .request(TwitchApi.ACCEPT)
                .get()
                .readEntity(Team.class);
    }
}
