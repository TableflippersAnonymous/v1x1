package tv.v1x1.common.services.twitch.dto.games;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameList {
    @JsonProperty
    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(final List<Game> games) {
        this.games = games;
    }
}
